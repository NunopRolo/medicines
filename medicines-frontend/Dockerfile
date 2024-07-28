FROM node AS build

WORKDIR /dist/src/app

RUN npm cache clean --force

COPY . .
RUN npm install
RUN npm run build_prod

FROM nginx:latest AS ngi

COPY --from=build /dist/src/app/dist/medicines-frontend /usr/share/nginx/html
COPY nginx_default.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
