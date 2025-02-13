FROM node:20.14.0-bullseye

ARG ENV_PROFILE
ENV ENV_PROFILE=${ENV_PROFILE}

WORKDIR /app

COPY . .

RUN npm install
RUN npm install -g pm2
RUN npm run build:${ENV_PROFILE}
RUN rm -rf ./src

EXPOSE 8002

CMD ["pm2-runtime", "start", "/app/dist/src/index.js", "--name", "zai-trading"]