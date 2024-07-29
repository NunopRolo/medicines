[issue]: https://github.com/NunopRolo/medicines/issues

![Medicines Backend Build](https://github.com/NunopRolo/medicines/actions/workflows/backend.yml/badge.svg)
![Medicines Backend Build](https://github.com/NunopRolo/medicines/actions/workflows/frontend.yml/badge.svg)
![Medicines Backend Tests Coverage](.github/badges/jacoco.svg)

# Medicines
Medicines app is a web application to manage portuguese medicines.

## Functionalities
* **Medicines List page**:
    * List of medicines with pagination
    * Search for medicines by any field
    * Search medicine by medicine barcode scan
* **Stock page**:
    * List of medicines in stock, with color code to identify which medicines are with validity, without validity, and less than 1 month validity
    * Search for medicines in stock, by any field
    * Filter medicines list by validity status
    * Add medicine to stock
    * Delete medicine from stock
    * Notification when medicine validity expires
* **Medication Page**:
    * Table with times of the day (Before Breakfast, Breakfast, etc) in the header
    * In the table cells, there are the medicines associated with a time of the day
    * Add medicine to a time of the day
    * Delete medicine from the time of the day
    * Add observations to the medicine entry
    * Add image to a medicine
    * Generate PDF of the medicines table
    * Add temporary medication
* **Settings Page:**
    * Add persons
    * Delete persons
    * Add day periods
    * Delete day periods

## Run Through Docker (Recommended)
```
docker-compose up
```

## Run separately

### Java Backend Service

* #### Environment Variables:
```
DATASOURCE_URL=<postgres_host>:5432/<postgres_host>
DATASOURCE_USERNAME=
DATASOURCE_PWD=
WEBCLIENT_URL=http://<HASS_HOST>/api/services/persistent_notification/create
HASS_TOKEN=
```
WEBCLIENT_URL and HASS_TOKEN are used for notification to Home Assistant.

* ### Build:
```
./gradlew build
```

* ### Run:
```
./gradlew bootRun
```

### Frontend

* ### Install:
```
npm install
```

* ### Run:
```
ng serve --host 0.0.0.0
```

* ### Open in the browser
```
http://localhost:4200
```

## Notes
* Medicines recognition by camera only works with SSL, or with local environment.

## Support
Open an issue [here][issue]