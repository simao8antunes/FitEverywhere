FROM node:alpine AS build
ARG VITE_API_BASE_URL
ENV VITE_API_BASE_URL=$VITE_API_BASE_URL
WORKDIR /app
COPY ../package.json .
RUN npm install
COPY .. .
RUN npm run build

# production environment
FROM nginx:alpine
EXPOSE 80
COPY docker/nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist /usr/share/nginx/html
