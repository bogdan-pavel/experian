**Build the project**

- mvn clean install

**Run the api**

- mvn spring-boot:run

***API call***
POST - localhost:8080/experian/creditScores
```
{
    "msg_id": 2,
    "directors_count": 2,
    "score": 40.12,
    "last_updated": "2021-10-10T11:11:11.111Z" ,
        "company_name":"Experian" ,
    "registration_date": "2020-10-27T14:34:06.132Z"
}
```

***TODO***


