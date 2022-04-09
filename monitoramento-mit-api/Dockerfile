FROM frolvlad/alpine-miniconda3

WORKDIR /app

COPY ./package.json ./

RUN apk add --update nodejs
RUN apk add --update yarn
# RUN apk add --update --no-cache python3 && ln -sf python3 /usr/bin/python
# RUN pip3 install --no-cache --upgrade pip setuptools
RUN pip install pandas
RUN pip install numpy
RUN pip install matplotlib
RUN yarn

COPY ./ ./

CMD ["yarn", "startDocker"]