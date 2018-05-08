FROM actframework/java-8
ENV APPHOME=/var/act-app
RUN mkdir -p $APPHOME
WORKDIR $APPHOME
ADD *.tar.gz $APPHOME

EXPOSE 5460/tcp
EXPOSE 5461/tcp

CMD $APPHOME/run
