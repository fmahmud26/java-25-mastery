# Practical: High CPU

## Symptoms

- `top` / container CPU throttling  
- Latency spikes  
- One or more JVMs pegged at ~N cores  

## Playbook

1. **Identify PID** and CPU%  
2. Sample stacks:  
   ```bash
   # several snapshots
   jcmd <pid> Thread.print > t1.txt
   sleep 2
   jcmd <pid> Thread.print > t2.txt
   ```  
3. Or **JFR** / **async-profiler**:  
   ```bash
   jcmd <pid> JFR.start name=cpu settings=profile
   # … reproduce …
   jcmd <pid> JFR.stop name=cpu filename=/tmp/cpu.jfr
   ```  
4. Find **hottest methods** — infinite loops, busy spin, regex, JSON, crypto, GC thrash  
5. If GC CPU high → heap pressure / leak ([gc-logs](./gc-logs.md))  
6. If one thread RUNNABLE tight loop → fix code  
7. If many threads in GC → memory  

## Distinguish

| Pattern | Likely |
|---------|--------|
| Same app method on every dump | Hot compute / loop |
| `GC task thread` dominating | Memory / GC |
| `epollWait` / socket read | Not CPU — misread RUNNABLE native |

Related: [thread-dump.md](./thread-dump.md), [gc-logs.md](./gc-logs.md).
