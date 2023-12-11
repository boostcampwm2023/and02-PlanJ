FROM node:20-alpine As development

WORKDIR /usr/src/planj

COPY package*.json ./

RUN npm install --include=dev

COPY . .

RUN npm run build

FROM node:20-alpine as production

WORKDIR /usr/src/app

COPY package*.json ./
COPY ecosystem-prod.config.js ./ecosystem.config.js

RUN npm install pm2 -g
RUN npm install --include=prod

COPY --from=development /usr/src/planj/dist ./dist

EXPOSE 3000
CMD ["pm2-runtime", "start", "ecosystem.config.js"]