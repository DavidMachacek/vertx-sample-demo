#!/bin/bash
while true;
do
  response=$(curl --location 'https://www.csast.csas.cz/webapi/api/v1/dfs/graphql?operationName=SearchProfiles' \
  --header 'Origin: https://www.csast.csas.cz' \
  --header 'Referer: https://www.csast.csas.cz/cs/dfs/komunita-dobrovolniku' \
  --header 'accept: */*' \
  --header 'content-type: application/json' \
  --header 'web-api-key: f23c580f-e534-4de7-9116-1c201d062405' \
  --header 'Cookie: TS010c6390=01a3067ae77351b2e6d9ce8d3304c29277ae61606e2dd671b871b8d6f4c6fcb855405caaacbfcb6d7cf14e925ffff3182a217d04e2' \
  --data '{"query":"fragment VolunteerInfo on Volunteer {\n  id\n  userAccount {\n    firstName\n    midName\n    lastName\n    __typename\n  }\n  state\n  __typename\n}\n\nfragment VolunteerProfileItem on VolunteerProfile {\n  id\n  volunteer {\n    ...VolunteerInfo\n    __typename\n  }\n  jobs\n  __typename\n}\n\nfragment VolunteerProfileSearchable on VolunteerProfileSearchable {\n  volunteerProfile {\n    ...VolunteerProfileItem\n    __typename\n  }\n  __typename\n}\n\nquery SearchProfiles($pageable: PaginationInput, $filter: VolunteerProfileSearchableFilterInput) {\n  searchProfiles(pageable: $pageable, filter: $filter) {\n    items {\n      ...VolunteerProfileSearchable\n      __typename\n    }\n    pageNumber\n    pageSize\n    nextPage\n    __typename\n  }\n}\n","variables":{"pageable":{"page":0,"size":18,"sort":"createdDate","order":"DESC"},"filter":{"volunteerState":["ACTIVE"]}}}')
  echo $response
  echo "Sleeping ..."
  sleep 5
done