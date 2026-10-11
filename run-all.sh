#!/bin/zsh
ROOT="${0:A:h}"
LOG_DIR="$ROOT/.logs"
mkdir -p "$LOG_DIR"

SERVICES=(account transaction reporting authentication frauddetection)
typeset -A PORTS
PORTS=(account 8080 transaction 8081 reporting 8082 authentication 8083 frauddetection 8084)

RESET=$'\e[0m'; DIM=$'\e[2m'
RED=$'\e[1;31m'; GREEN=$'\e[1;32m'; YELLOW=$'\e[1;33m'; CYAN=$'\e[1;36m'; BLUE=$'\e[1;94m'

port_open() { nc -z localhost "$1" >/dev/null 2>&1 }

kill_tree() {
  local pid=$1 child
  for child in $(pgrep -P "$pid" 2>/dev/null); do kill_tree "$child"; done
  kill "$pid" 2>/dev/null
}

# Kill anything listening on the service ports
free_ports() {
  local name port pids found=0
  for name in $SERVICES; do
    port=${PORTS[$name]}
    pids=$(lsof -tiTCP:$port -sTCP:LISTEN 2>/dev/null)
    if [[ -n $pids ]]; then
      found=1
      printf '%s\n' "${YELLOW}Killing port $port (PID ${pids//$'\n'/, })${RESET}"
      kill ${=pids} 2>/dev/null
    fi
  done
  (( found )) || return
  sleep 2
  for name in $SERVICES; do
    pids=$(lsof -tiTCP:${PORTS[$name]} -sTCP:LISTEN 2>/dev/null)
    [[ -n $pids ]] && kill -9 ${=pids} 2>/dev/null
  done
}

CLEANED=0
cleanup() {
  (( ZSH_SUBSHELL )) && return
  (( CLEANED )) && return
  CLEANED=1
  printf '\e[?25h\n%s\n' "${YELLOW}Stopping all services...${RESET}"
  local child
  for child in $(pgrep -P $$ 2>/dev/null); do kill_tree "$child"; done
  free_ports
  printf '%s\n' "${GREEN}All services stopped, ports 8080-8084 are free.${RESET}"
  exit 0
}
trap cleanup INT TERM EXIT

last_error() {
  local log="$LOG_DIR/$1.log" line
  line=$(grep -aE 'ERROR|Exception|Description:|BUILD FAILURE|not found|Caused by|rename' "$log" 2>/dev/null | tail -1)
  [[ -z $line ]] && line=$(grep -av '^\s*$' "$log" 2>/dev/null | tail -1)
  print -r -- "$line" | sed $'s/\e\\[[0-9;]*m//g'
}

# Runs in the background. The exit code is written to .logs/<name>.exit when the app ends.
launch_service() {
  local name=$1 port=${PORTS[$name]}
  local dir="$ROOT/$name" log="$LOG_DIR/$name.log" exitf="$LOG_DIR/$name.exit"
  local -a cmd warm

  if [[ ! -d $dir ]]; then
    echo "ERROR: directory '$name' not found" >> "$log"; echo 127 > "$exitf"; return
  fi
  if   [[ -x $dir/mvnw ]];      then cmd=(./mvnw spring-boot:run); warm=(./mvnw -v)
  elif [[ -f $dir/pom.xml ]];   then cmd=(mvn spring-boot:run)
  elif [[ -x $dir/gradlew ]];   then cmd=(./gradlew bootRun);      warm=(./gradlew --version)
  elif [[ -f $dir/build.gradle || -f $dir/build.gradle.kts ]]; then cmd=(gradle bootRun)
  else
    echo "ERROR: no pom.xml or build.gradle found" >> "$log"; echo 127 > "$exitf"; return
  fi

  # Prepare wrappers one at a time to avoid parallel download clashes
  if (( ${#warm} )); then
    if ! (cd "$dir" && "${warm[@]}" >> "$log" 2>&1 < /dev/null); then
      echo "ERROR: wrapper setup failed" >> "$log"; echo 1 > "$exitf"; return
    fi
  fi

  (
    cd "$dir" || exit 1
    SERVER_PORT=$port "${cmd[@]}" < /dev/null >> "$log" 2>&1
    echo $? > "$exitf"
  ) &
}

service_status() {
  local name=$1 code
  if [[ -f $LOG_DIR/$name.exit ]]; then
    code=$(cat "$LOG_DIR/$name.exit")
    if [[ $code == 0 ]]; then
      STATE="FINISHED"; DETAIL="Completed successfully (exit 0)"
    else
      STATE="CRASHED"; DETAIL="exit $code: $(last_error $name)"
    fi
  elif port_open "${PORTS[$name]}"; then
    STATE="RUNNING"; DETAIL="http://localhost:${PORTS[$name]}"
  elif [[ -s $LOG_DIR/$name.log ]]; then
    STATE="LOADING"; DETAIL="Starting, waiting for port ${PORTS[$name]}..."
  else
    STATE="LOADING"; DETAIL="Preparing..."
  fi
}

draw() {
  local cols=$(tput cols 2>/dev/null || echo 100)
  local running=0 finished=0 total=${#SERVICES} name color dcolor max detail
  printf '\e[H'
  printf '%s\e[K\n' "${CYAN}  My-Bank | Service Dashboard${RESET}"
  printf '%s\e[K\n' "${DIM}  ${(l:$((cols-4))::─:)}${RESET}"
  for name in $SERVICES; do
    service_status "$name"
    case $STATE in
      RUNNING)  color=$GREEN;  dcolor=$DIM; (( running++ )) ;;
      FINISHED) color=$BLUE;   dcolor=$DIM; (( finished++ )) ;;
      LOADING)  color=$YELLOW; dcolor=$DIM ;;
      *)        color=$RED;    dcolor=$RED ;;
    esac
    max=$(( cols - 44 )); (( max < 20 )) && max=20
    detail="${DETAIL[1,$max]}"
    printf '  %b●%b %-16s :%-5s %b%-9s%b %b%s%b\e[K\n' \
      "$color" "$RESET" "$name" "${PORTS[$name]}" "$color" "$STATE" "$RESET" "$dcolor" "$detail" "$RESET"
  done
  printf '%s\e[K\n' "${DIM}  ${(l:$((cols-4))::─:)}${RESET}"
  local ok=$(( running + finished ))
  local sc=$GREEN; (( ok < total )) && sc=$YELLOW; (( ok == 0 )) && sc=$RED
  printf '  %b%d running | %d finished | %d total%b   %sCtrl+C stops all and frees the ports | logs in .logs/%s\e[K\n' \
    "$sc" "$running" "$finished" "$total" "$RESET" "$DIM" "$RESET"
  printf '\e[J'
}

# Free the ports first, then reset state so nothing starts out as CRASHED
free_ports
rm -f "$LOG_DIR"/*.exit
for name in $SERVICES; do : > "$LOG_DIR/$name.log"; done

# Start everything in the background so the dashboard appears immediately
( for name in $SERVICES; do launch_service "$name"; done; wait ) &

printf '\e[2J\e[?25l'
while true; do draw; sleep 1; done
