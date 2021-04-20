FROM gradle:jdk11
EXPOSE 9002/tcp
COPY --chown=gradle:gradle . /prior-auth-rules-engine/
RUN apt-get update         
RUN apt-get install -y git
WORKDIR /prior-auth/
RUN git clone https://github.com/HL7-DaVinci/CDS-Library.git
RUN gradle installBootDist
CMD ["gradle", "bootRun"]
