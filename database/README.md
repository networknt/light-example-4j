To build and start the example database.

## Environment

You have to make sure you have docker and docker compose installed and you are in
light-java-example/database folder.

## Build

```
mvn clean install
docker build -t networknt/example-database .
```
## Compose

```
docker-compose up
```

## Performance

Just did a single query and the result:

```
steve@joy:~/tool/wrk$ wrk -t12 -c400 -d30s http://localhost:8080/db/postgres
Running 30s test @ http://localhost:8080/db/postgres
  12 threads and 400 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    28.43ms   65.72ms   1.23s    98.62%
    Req/Sec     1.55k   239.88     3.46k    82.87%
  552751 requests in 30.10s, 82.65MB read
Requests/sec:  18364.51
Transfer/sec:      2.75MB

steve@joy:~/tool/wrk$ wrk -t12 -c400 -d30s http://localhost:8080/db/mysql
Running 30s test @ http://localhost:8080/db/mysql
  12 threads and 400 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    42.95ms   43.06ms   1.17s    98.33%
    Req/Sec   824.96    125.55     1.84k    88.67%
  295912 requests in 30.10s, 44.24MB read
Requests/sec:   9831.21
Transfer/sec:      1.47MB


```

Multiple queries, 10 queries per requests.

```
steve@joy:~/tool/wrk$ wrk -t12 -c400 -d30s http://localhost:8080/queries/postgres?queries=10
Running 30s test @ http://localhost:8080/queries/postgres?queries=10
  12 threads and 400 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   167.06ms   90.73ms   1.47s    88.58%
    Req/Sec   207.01     32.11   313.00     69.60%
  74046 requests in 30.05s, 31.48MB read
Requests/sec:   2463.88
Transfer/sec:      1.05MB


steve@joy:~/tool/wrk$ wrk -t12 -c400 -d30s http://localhost:8080/queries/mysql?queries=10
Running 30s test @ http://localhost:8080/queries/mysql?queries=10
  12 threads and 400 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   225.87ms   83.60ms   1.56s    89.99%
    Req/Sec   148.55     30.53   270.00     67.50%
  53255 requests in 30.08s, 22.64MB read
Requests/sec:   1770.43
Transfer/sec:    770.74KB

```


Updates

```
steve@joy:~/tool/wrk$ wrk -t12 -c400 -d30s http://localhost:8080/updates/postgres
Running 30s test @ http://localhost:8080/updates/postgres
  12 threads and 400 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    43.64ms   27.70ms 603.83ms   90.85%
    Req/Sec   797.76    180.00     1.09k    78.87%
  283215 requests in 30.03s, 42.89MB read
Requests/sec:   9431.96
Transfer/sec:      1.43MB

steve@joy:~/tool/wrk$ wrk -t12 -c400 -d30s http://localhost:8080/updates/mysql
Running 30s test @ http://localhost:8080/updates/mysql
  12 threads and 400 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    76.83ms   15.97ms 259.81ms   87.03%
    Req/Sec   431.27     41.30   565.00     82.54%
  154740 requests in 30.09s, 23.43MB read
Requests/sec:   5142.88
Transfer/sec:    797.44KB


```

Conclusion: postgres has better performance then mysql on my i5 desktop with 32GB memory and SSD.
