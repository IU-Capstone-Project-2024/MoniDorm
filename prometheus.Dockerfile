#
# PACKAGE STAGE
#
FROM prom/prometheus
COPY prometheus/prometheus.yml /etc/prometheus/