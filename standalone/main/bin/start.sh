java -cp "../config/:../lib/jetty-runner.jar" \
		-Dconfig.resource=/jwebconsole.conf \
		-Dcom.sun.management.jmxremote \
		-Dcom.sun.management.jmxremote.port=1099 \
		-Dcom.sun.management.jmxremote.local.only=false \
		-Dcom.sun.management.jmxremote.authenticate=false \
		-Dcom.sun.management.jmxremote.ssl=false \
		org.mortbay.jetty.runner.Runner ../lib/jwebconsole-client.war