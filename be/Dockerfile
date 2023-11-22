FROM node:20-alpine As development

WORKDIR /usr/src/planj

COPY package*.json ./

RUN npm install --only=development

COPY . .

RUN npm run build

FROM node:20-alpine as production

ARG NODE_ENV=stage
ENV NODE_ENV=${NODE_ENV}

WORKDIR /usr/src/app

COPY package*.json ./

RUN npm install --only=production

COPY . .
COPY --from=development /usr/src/planj/dist ./dist

EXPOSE 3000
CMD ["node", "dist/main"]