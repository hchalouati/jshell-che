# Copyright (c) 2012-2016 Codenvy, S.A.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
# Contributors:
# Codenvy, S.A. - initial API and implementation

FROM ubuntu:16.04

ENV JAVA_HOME=/usr/lib/jvm/java-10-oracle/
ENV PATH=$JAVA_HOME/bin:$PATH
RUN apt-get update && \
    apt-get -y install \
    locales \
    rsync \
    openssh-server \
    sudo \
    procps \
    wget \
    unzip \
    mc \
    ca-certificates \
    curl \
    software-properties-common \
    python-software-properties \
    bash-completion && \
    mkdir /var/run/sshd && \
    sed 's@session\s*required\s*pam_loginuid.so@session optional pam_loginuid.so@g' -i /etc/pam.d/sshd && \
    echo "%sudo ALL=(ALL) NOPASSWD: ALL" >> /etc/sudoers && \
    useradd -u 1000 -G users,sudo -d /home/user --shell /bin/bash -m user && \
    usermod -p "*" user && \
    add-apt-repository ppa:git-core/ppa && \
    add-apt-repository ppa:linuxuprising/java && \
    apt-get update && \
    sudo apt-get install git subversion -y && \
    apt-get clean && \
    apt-get -y autoremove && \
    echo "oracle-java8-installer shared/accepted-oracle-license-v1-1 select true" | debconf-set-selections && \
    echo "oracle-java8-installer shared/accepted-oracle-license-v1-1 seen true" | debconf-set-selections && \
    sudo apt-get install -y oracle-java10-installer && \
    sudo update-ca-certificates -f && \
    apt-get -y clean && \
    rm -rf /var/lib/apt/lists/*

ENV LANG en_GB.UTF-8
ENV LANG en_US.UTF-8
USER user
RUN sudo locale-gen en_US.UTF-8 && \
    svn --version && \
    cd /home/user && ls -la && \
    sed -i 's/# store-passwords = no/store-passwords = yes/g' /home/user/.subversion/servers && \
    sed -i 's/# store-plaintext-passwords = no/store-plaintext-passwords = yes/g' /home/user/.subversion/servers
EXPOSE 22 4403
WORKDIR /projects

# The following instructions set the right
# permissions and scripts to allow the container
# to be run by an arbitrary user (i.e. a user
# that doesn't already exist in /etc/passwd)
ENV HOME /home/user
RUN for f in "/home/user" "/etc/passwd" "/etc/group" "/projects"; do\
           sudo chgrp -R 0 ${f} && \
           sudo chmod -R g+rwX ${f}; \
        done && \
        # Generate passwd.template \
        cat /etc/passwd | \
        sed s#user:x.*#user:x:\${USER_ID}:\${GROUP_ID}::\${HOME}:/bin/bash#g \
        > /home/user/passwd.template && \
        # Generate group.template \
        cat /etc/group | \
        sed s#root:x:0:#root:x:0:0,\${USER_ID}:#g \
        > /home/user/group.template && \
        sudo sed -ri 's/StrictModes yes/StrictModes no/g' /etc/ssh/sshd_config
EXPOSE 4403 8000 8080 9876 22

LABEL che:server:8080:ref=tomcat8 che:server:8080:protocol=http che:server:8000:ref=tomcat8-debug che:server:8000:protocol=http che:server:9876:ref=codeserver che:server:9876:protocol=http

ENV MAVEN_VERSION=3.3.9 \
    JAVA_HOME=/usr/lib/jvm/java-10-oracle \
    TOMCAT_HOME=/home/user/tomcat8 \
    TERM=xterm
ENV M2_HOME=/home/user/apache-maven-$MAVEN_VERSION
ENV PATH=$JAVA_HOME/bin:$M2_HOME/bin:$PATH


RUN mkdir /home/user/tomcat8 /home/user/apache-maven-$MAVEN_VERSION && \
    wget -qO- "http://apache.ip-connect.vn.ua/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz" | tar -zx --strip-components=1 -C /home/user/apache-maven-$MAVEN_VERSION/ && \
    wget -qO- "http://archive.apache.org/dist/tomcat/tomcat-8/v8.0.24/bin/apache-tomcat-8.0.24.tar.gz" | tar -zx --strip-components=1 -C /home/user/tomcat8 && \
    rm -rf /home/user/tomcat8/webapps/* && \
    echo "export MAVEN_OPTS=\$JAVA_OPTS" >> /home/user/.bashrc && \
    sudo chgrp -R 0 ~/tomcat8 && \
    sudo chmod -R g+rwX ~/tomcat8

RUN mkdir /home/user/classpath  && chmod -R 777 /home/user/classpath

COPY  ["charting.jar","/home/user/classpath/charting.jar"]

COPY  ["suanshu-20120606.jar","/home/user/classpath/suanshu-20120606.jar"]

RUN mkdir /home/user/charting  && chmod -R 777 /home/user/charting/

COPY ["app.war","/home/user/charting/app.war"]


COPY ["start_charting.sh","/tmp/start_charting.sh"]

RUN mkdir /home/user/scripts  && cp /tmp/start_charting.sh /home/user/scripts/ && chmod 777 /home/user/scripts/start_charting.sh



ENV CLASSPATH=/home/user/classpath/charting.jar:/home/user/classpath/suanshu-20120606.jar



COPY ["main","/home/user/scripts/main"]
COPY ["hellosuanshu","/home/user/scripts/hellosuanshu"]
COPY ["README","/home/user/scripts/README"]


COPY ["entrypoint.sh","/home/user/entrypoint.sh"]
ENTRYPOINT ["/home/user/entrypoint.sh"]
CMD tail -f /dev/null