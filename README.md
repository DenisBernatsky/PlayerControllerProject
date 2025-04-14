| №  | Severity  | Description                                                                                     | Expected Behavior / Comment                                            |
|----|-----------|-------------------------------------------------------------------------------------------------|------------------------------------------------------------------------|
| 1  | Critical  | Cannot create a record with the `admin` role                                                    | The `admin` role should be supported during user creation              |
| 2  | Critical  | REST principles are violated: `GET` is used for creation, and `POST` for retrieval              | Use `POST` for creation, `GET` for retrieval                           |
| 3  | Critical  | Records can be deleted by any `editor`, even without `admin`/`supervisor` role                  | There should be strict role validation for `editor`                    |
| 4  | Critical  | Re-creating a user with the same `login` updates data instead of throwing an error              | Should return an error: "login already exists"                         |
| 5  | Critical  | No validation of `editor` on deletion: any string allows record deletion                        | There should be validation for allowed values of `editor`              |
| 6  | Critical  | `/player/get` response includes password and login, even without authorization                  | These fields should not be returned. Authorization should be required  |

| №  | Severity | Description                                                                                          | Expected Behavior / Comment                                             |
|----|----------|------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------|
| 1  | Major    | After deletion, a request to the same ID returns `200` instead of indicating the record is gone     | Should return `404` if the record is not found                          |
| 2  | Major    | After player creation, some fields in the response come back as `null`                              | The response should return all fields as provided                        |
| 3  | Major    | Swagger includes a controller for retrieving all players, which is not described in the requirements| The controller should be either removed or documented properly           |
| 4  | Major    | `password` is not marked as required when creating a player                                         | The `password` field should be marked as required                        |
| 5  | Major    | `screenName` is not unique                                                                          | There should be uniqueness validation for `screenName`                  |
| 6  | Major    | Password length validation does not work (`min 7`, `max 15` characters)                             | Proper length validation should be added                                |
| 7  | Major    | No validation for `gender` value — any random value is accepted                                     | Validation should enforce allowed values: `male`, `female`, etc.        |
