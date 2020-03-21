# COVID 19 data API

This api built using Spring boot. It has three endpoints.
1) /getAllWorldData - Gives the data in json for all the countries
2) /getIndianStatesData - Gives the data about all the indian states
3) /getCasesByCountry - Gives the data of individual countries

Also there is another api endipoint, 

**/retrieveDataFromSources** <br />
    This endpoint retrieves the data from the sources, this is scheduled to run for every hour. 
Also! call this endpoint for the first time when app got deployed.

Data Sources
1) https://www.mohfw.gov.in/
2) https://www.worldometers.info/coronavirus/
