Execute Load
============
k6 run -d 30s -u 30 ./scripts/script.js

Install Prometheus
==================
docker run -d -p 9090:9090 -v ./scripts/prometheus.yml:/etc/prometheus/prometheus.yml prom/prometheus

Access Prometheus
=================
http://localhost:9090

Install Grafana
===============
docker run -d -p 3000:3000 grafana/grafana

Access Grafana
==============
http://localhost:3000 [admin/admin]