docker run --rm -v C:/Users/kchandrakant/Desktop/jmeter-docker/scripts:/mnt/jmeter jmeter -n -t /mnt/jmeter/jmeter-script.jmx -l /mnt/jmeter/result.jtl -j /mnt/jmeter/jmeter.log

docker run --rm -v C:/Users/kchandrakant/Desktop/jmeter-docker/scripts:/bzt-configs -v C:/Users/kchandrakant/Desktop/jmeter-docker/artifacts:/tmp/artifacts blazemeter/taurus