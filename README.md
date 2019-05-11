# API

Notes:

    - replace http://localhost:8080 with real host
    - responses well-formatted for readability
    - there are no space symbols in real responses
    - here, fake tokens just for example

## Registration

Request:
```bash
curl -X POST \
    -H 'Content-Type: application/json' \
    --data '{"username":"dima","password":"123456"}' \
    http://localhost:8080/register
```
Response:
```json
{"status": "OK"}
```

Request (send for the second time):
```bash
curl -X POST \
    -H 'Content-Type: application/json' \
    --data '{"username":"dima","password":"123456"}' \
    http://localhost:8080/register
```

Response (with error):
```json
{
  "errorMessage": "username is already taken",
  "status": "ERROR"
}
```


## Authentication:

Request:
```bash
curl -X POST \
    -H 'Content-Type: application/json' \
    --data '{"username":"dima","password":"123456"}' \
    http://localhost:8080/auth
```

Response:
```json
{
  "status": "OK",
  "token": "dima2019-04-10T19:25:07.916"
}
```

Request (wrong password):
```bash
curl -X POST \
    -H 'Content-Type: application/json' \
    --data '{"username":"dima","password":"_"}' \
    http://localhost:8080/auth
```

Response:
```json
{
  "errorMessage": "Wrong username or password",
  "status": "ERROR"
}
```

## Subscriptions:

Request (subscribe) (full podcast info):
```bash
curl -X POST \
    -H 'Content-Type: application/json' \
    --data '{"token":"dima2019-04-10T19:25:07.916","podcastId":1001642707,"podcastFeedUrl":"https://arzamas.academy/feed_v1/podcast.rss"}' \
    http://localhost:8080/subscriptions
```
Default status_ok response.

Request (subscribe) (with no podcast info):
```bash
curl -X POST \
    -H 'Content-Type: application/json' \
    --data '{"token":"dima2019-04-13T16:44:04.634612200","podcastId":1209828744}' \
    http://localhost:8080/subscriptions
```
Default status_ok response.

Request (get subscriptions):
```bash
curl -X GET \
    -H 'Content-Type: application/json' \
    --data '{"token":"dima2019-04-13T16:44:04.634612200"}' \
    http://localhost:8080/subscriptions
```
Response:
```json
{
  "status": "OK",
  "subscriptions": [
    {
      "id": 1001642707,
      "feedUrl": "https://arzamas.academy/feed_v1/podcast.rss"
    },
    {
      "id": 1001,
      "feedUrl": null
    },
    {
      "id": 1209828744,
      "feedUrl": null
    }
  ]
}
```

Request (unsubscribe):
```bash
curl -X DELETE \
    -H 'Content-Type: application/json' \
    --data '{"token":"dima2019-04-10T19:25:07.916","podcastId":1001642707}' \
    http://localhost:8080/subscriptions
```
Default status_ok response.

## History:

Request (add to history):
```bash
curl -X POST \
    -H 'Content-Type: application/json' \
    --data '{"token":"dima2019-04-13T16:44:04.634612200","episodeGuid":"tag:soundcloud,2010:tracks/594968265"}' \
    http://localhost:8080/history
```
Default STATUS OK response.

Request (add to history, with extra info):
```bash
curl -X POST \
    -H 'Content-Type: application/json' \
    --data '{"token":"dima2019-04-13T16:44:04.634612200","episodeGuid":"tag:soundcloud,2010:tracks/590415684","podcastId":1162673070,"episodeLink":"http://feeds.soundcloud.com/stream/594968265-mimpod-episode_27.mp3"}' \
    http://localhost:8080/history
```
Default STATUS OK response.

Request (get your history):
```bash
curl -X GET \
    -H 'Content-Type: application/json' \
    --data '{"token":"dima2019-04-13T16:44:04.634612200"}' \
    http://localhost:8080/history
```
Response:
```json
{
  "history": [
    {
      "guid": "tag:soundcloud,2010:tracks/590415684",
      "podcastId": 1162673070,
      "link": "http://feeds.soundcloud.com/stream/594968265-mimpod-episode_27.mp3"
    },
    {
      "guid": "tag:soundcloud,2010:tracks/594968265",
      "podcastId": null,
      "link": null
    }
  ],
  "status": "OK"
}
```


## Progress:

Request (create or update progress):

Specify episode guid, time in seconds.

Request (get all my progress):
```bash
curl -X GET \
    -H 'Content-Type: application/json' \
    --data '{"token":"dima2019-04-13T16:44:04.634612200"}' \
    http://localhost:8080/progress
```
Response:
```json
{
  "progress": [
    {
      "accountId": 1,
      "episodeGuid": "tag:soundcloud,2010:tracks/590415684",
      "time": 10
    },
    {
      "accountId": 1,
      "episodeGuid": "1",
      "time": 1120
    }
  ],
  "status": "OK"
}
```

Request (get progress on specific episode):
```bash
curl -X GET \
    -H 'Content-Type: application/json' \
    --data '{"token":"dima2019-04-13T16:44:04.634612200","guid":"tag:soundcloud,2010:tracks/590415684"}' \
    http://localhost:8080/progress
```

Request (save/update progress):
```bash
curl -X POST \
    -H 'Content-Type: application/json' \
    --data '{"token":"dima2019-04-13T16:44:04.634612200","guid":"tag:soundcloud,2010:tracks/590415684","time":10}' \
    http://localhost:8080/progress
```
Default STATUS OK response.

## Play later:
Get play later request:
```bash
curl -X GET \
    -H 'Content-Type: application/json' \
    --data '{"token":"dima2019-04-13T16:44:04.634612200"}' \
    http://localhost:8080/play_later
```

Response:
```json
{
    "playLater": [
        {
            "guid": "tag:soundcloud,2010:tracks/590415684",
            "podcastId": 1162673070,
            "link": "http://feeds.soundcloud.com/stream/594968265-mimpod-episode_27.mp3"
        },
        {
            "guid": "hello",
            "podcastId": null,
            "link": null
        }
    ],
    "status": "OK"
}
```

Add to play later request:
```bash
curl -X POST \
    -H 'Content-Type: application/json' \
    --data '{"token":"dima2019-04-13T16:44:04.634612200","guid":"tag:soundcloud,2010:tracks/590415684"}'
    http://localhost:8080/play_later
```
Default status ok response.

Delete request:
```bash
curl -X DELETE \
    -H 'Content-Type: application/json' \
    --data '{"token":"dima2019-04-13T16:44:04.634612200","guid":"123"}'
    http://localhost:8080/play_later
```
Default status ok response.
