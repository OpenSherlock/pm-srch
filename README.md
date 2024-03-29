# pm-srch

## Flow
#### Query
- Text files of query terms
- Query terms sent electronically

#### ProcessQuery
- Query to `QueryEngine`
- Engine Thread --> `runQuery`
- `Carrot2` run the query and return collection of PubMed abstracts - no clustering
-  