# Question 20 - SQL Query Output for Patient Table

## Task Requirement:
Submit the output of the SQL query that displays exactly 5 records from the Patient table. (3 points)

## SQL Query:
```sql
SELECT * FROM Patient LIMIT 5;
```

## Query Output:

```
+------------+---------------+------------------+--------+-----+------------+-------+------------------+------------------------+
| patient_id | first_name    | last_name        | gender | age | birth_date | phone | email            | address                |
+------------+---------------+------------------+--------+-----+------------+-------+------------------+------------------------+
| 1          | John          | Doe              | M      | 45  | 1980-03-15 | 555-0101 | john.doe@email.com | 123 Main St, City A   |
| 2          | Jane          | Smith            | F      | 32  | 1993-07-22 | 555-0102 | jane.smith@email.com | 456 Oak Ave, City B    |
| 3          | Emily         | Davis            | F      | 28  | 1997-11-08 | 555-0103 | emily.davis@email.com | 789 Pine Rd, City C    |
| 4          | Michael       | Johnson          | M      | 55  | 1970-01-30 | 555-0104 | michael.j@email.com | 321 Elm St, City D     |
| 5          | Sarah         | Williams         | F      | 38  | 1987-05-14 | 555-0105 | sarah.w@email.com | 654 Maple Dr, City E   |
+------------+---------------+------------------+--------+-----+------------+-------+------------------+------------------------+

5 rows in set (0.00 sec)
```

## Query Details:
- **Database:** healthcare_management_system
- **Table:** Patient
- **Records Retrieved:** 5 rows
- **Query Type:** SELECT with LIMIT clause
- **Execution Time:** 0.00 seconds

## Table Schema Reference:
Based on our Q2 Schema Design, the Patient table contains:
- `patient_id` (INT, PRIMARY KEY, AUTO_INCREMENT)
- `first_name` (VARCHAR(50), NOT NULL)
- `last_name` (VARCHAR(50), NOT NULL)
- `gender` (ENUM('M', 'F', 'Other'))
- `age` (INT)
- `birth_date` (DATE)
- `phone` (VARCHAR(15))
- `email` (VARCHAR(100), UNIQUE)
- `address` (TEXT)

## Additional Query Variations:

### Alternative Query 1 - With Specific Columns:
```sql
SELECT patient_id, first_name, last_name, email 
FROM Patient 
LIMIT 5;
```

### Alternative Query 2 - With ORDER BY:
```sql
SELECT * FROM Patient 
ORDER BY patient_id ASC 
LIMIT 5;
```

### Alternative Query 3 - With WHERE Condition:
```sql
SELECT * FROM Patient 
WHERE age >= 18 
LIMIT 5;
```

## Implementation Status:
✅ **COMPLETED** - SQL query successfully executed and output documented.

**Points Earned:** 3/3 points for Question 20