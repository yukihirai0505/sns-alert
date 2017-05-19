## Get Started

### Deploying to Heroku

```sh
$ git clone https://github.com/yukihirai0505/sns-alert.git
$ cd sns-alert
$ heroku git:remote -a sns-alert
$ git push heroku master
$ heroku open
```

### heroku settings

```
$ heroku config:add APPLICATION_SECRET=
$ heroku config:add FACEBOOK_ID=
$ heroku config:add FACEBOOK_SECRET=
$ heroku config:add GMAIL_PASS=
$ heroku config:add GMAIL_USER=
$ heroku config:add INSTAGRAM_CLIENT_ID=
$ heroku config:add INSTAGRAM_SECRET=
$ heroku config:add TWITTER_ACCESS_TOKEN_KEY=
$ heroku config:add TWITTER_ACCESS_TOKEN_SECRET=
$ heroku config:add TWITTER_CONSUMER_TOKEN_KEY=
$ heroku config:add TWITTER_CONSUMER_TOKEN_SECRET=
```

## Local


### DB setting

create user, db

```
$ createuser -P root
$ createdb sns_alert -O root
```

please use ddl at `conf/evolutions.default/*.sql`


### Env Setting

```
$ cp env.text.default env.text # set env
$ eval (env.text)
```