FROM node:alpine
WORKDIR /app
ENV PATH /app/node_modules/.bin:$PATH
COPY package.json /app/package.json
RUN npm i
COPY . /app
EXPOSE 3000
CMD ["npm", "run", "dev"]