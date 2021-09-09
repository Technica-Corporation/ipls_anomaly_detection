FROM node:carbon

# install yarn
RUN apt-get update && apt-get install apt-transport-https

RUN curl -sS https://dl.yarnpkg.com/debian/pubkey.gpg | apt-key add -
RUN echo "deb https://dl.yarnpkg.com/debian/ stable main" | tee /etc/apt/sources.list.d/yarn.list

RUN apt-get update && apt-get install yarn -y

# Create app directory
WORKDIR /usr/src/ad-gui

# Install app dependencies
COPY package*.json ./
RUN yarn install

# If you are running your code for production
# RUN yarn install --production

# Bundle app source
COPY . .

# Expose the port the server will listen on
EXPOSE 5601 
CMD [ "node", "app.js" ]
