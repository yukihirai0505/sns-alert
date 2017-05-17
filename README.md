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
$ heroku config:add APPLICATION_SECRET={it depends on you}
```

## Local

create user, db

```
createuser -P root
createdb sns_alert -O root
```