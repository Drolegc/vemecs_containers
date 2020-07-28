FROM node:10

# Create app directory
WORKDIR /usr/src/app

# Install app dependencies
COPY package*.json ./
RUN npm install

# Bundle app source
COPY . /usr/src/app
ARG APP_NODE_ENV=production
ENV NODE_ENV ${NODE_ENV}

EXPOSE 8080

ENV HOST 0.0.0.0

CMD [ "node","main.js" ]