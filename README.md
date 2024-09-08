# Parli Pulse

## GovHack 2024



## Data Sources

### [Federal Parliament- House of Reps - Official Hansard](https://www.aph.gov.au/Parliamentary_Business/Hansard)

Description: The Federal Government Hansard is the official, edited transcript of the proceedings of the Australian Parliament. It records debates, speeches, questions, and other parliamentary business

Usage: I wrote a small program that [retrieves](src/psithur/govhack/get_data.clj) the House of Reps Hansards since 2012 in XML format and then [parses](src/psithur/govhack/process_xml.clj) those XML Documents into a JSON file with the following fields :-

```json
{
  "session.no": "1",
  "info-type": "PRIVILEGE",
  "date": "2018-11-29",
  "speaker-name": "Morton, Ben, MP",
  "speaker-electorate": "Tangney",
  "chamber": "House of Reps",
  "page.no": "0",
  "proof": "0",
  "parliament.no": "45",
  "speaker-party": "LP",
  "info-title": "PRIVILEGE",
  "period.no": "7",
  "text": "..."
}
```
The dataset I created for GovHack covers the following years :-

| Year | Record Count |
| --- | --- |
| 2024 | 7730 |
| 2023 | 11996 |
| 2022 | 7746 |
| 2021 | 11866 |
| 2020 | 10318 |
| 2019 | 8166 |
| 2018 | 11128 |
| 2017 | 11400 |
| 2016 | 8982 |
| 2015 | 13500 |
| 2014 | 12664 |
| 2013 | 7884 |
| 2012 | 11066 |


The types of Speech included over that timeframe :-

| info_title |	record_count	|
| --- | --- |
|	BILLS	|	50238	|
|	STATEMENTS BY MEMBERS	|	26150	|
|	MATTERS OF PUBLIC IMPORTANCE	|	12056	|
|	ADJOURNMENT	|	9752	|
|	COMMITTEES	|	7684	|
|	MOTIONS	|	5608	|
|	PRIVATE MEMBERS' BUSINESS	|	4430	|
|	BUSINESS	|	2674	|
|	CONDOLENCES	|	2206	|
|	DOCUMENTS	|	1792	|
|	DISTINGUISHED VISITORS	|	1556	|
|	STATEMENTS ON INDULGENCE	|	1392	|
|	MINISTERIAL STATEMENTS	|	1018	|
|	PERSONAL EXPLANATIONS	|	870	|
|	PETITIONS	|	838	|
|	GOVERNOR-GENERAL'S SPEECH	|	820	|
|	AUDITOR-GENERAL'S REPORTS	|	560	|
|	STATEMENTS	|	550	|
|	STATEMENT BY THE SPEAKER	|	546	|
|	PARLIAMENTARY REPRESENTATION	|	448	|
|	MINISTERIAL ARRANGEMENTS	|	438	|
|	PARLIAMENTARY OFFICE HOLDERS	|	402	|
|	RESOLUTIONS OF THE SENATE	|	324	|
|	QUESTIONS WITHOUT NOTICE	|	296	|
|	QUESTIONS WITHOUT NOTICE: ADDITIONAL ANSWERS	|	272	|
|	DELEGATION REPORTS	|	270	|
|	DEATH OF HER MAJESTY QUEEN ELIZABETH II AND ACCESSION OF HIS MAJESTY KING CHARLES III	|	226	|
|	PRIVILEGE	|	146	|
|	STATEMENTS ON SIGNIFICANT MATTERS	|	138	|
|	REGULATIONS AND DETERMINATIONS	|	134	|
|	MINISTRY	|	132	|
|	QUESTIONS TO THE SPEAKER	|	120	|
|	REGISTER OF MEMBERS' INTERESTS	|	52	|
|	TARIFF PROPOSALS	|	50	|
|	PARTY OFFICE HOLDERS	|	46	|
|	SHADOW MINISTERIAL ARRANGEMENTS	|	42	|
|	NOTICES	|	38	|
|	PARLIAMENTARY ZONE	|	32	|
|	BUDGET	|	22	|
|	SHADOW MINISTRY	|	10	|
|	ADDRESS BY THE PRIME MINISTER OF THE UNITED KINGDOM	|	8	|
|	ADDRESS BY THE PRESIDENT OF THE PEOPLE'S REPUBLIC OF CHINA	|	8	|
|	ADDRESS BY THE PRIME MINISTER OF THE REPUBLIC OF INDIA	|	8	|
|	PRIME MINISTER OF PAPUA NEW GUINEA	|	6	|
|	PRESIDENT OF THE REPUBLIC OF THE PHILIPPINES	|	6	|
|	ADDRESS BY THE PRESIDENT OF THE REPUBLIC OF INDONESIA	|	6	|
|	ADDRESS BY THE PRIME MINISTER OF JAPAN	|	6	|
|	PRESIDENT OF UKRAINE	|	6	|
|	PARLIAMENTARY RETIRING ALLOWANCES TRUST	|	6	|
|	ADDRESS BY THE PRIME MINISTER OF SINGAPORE	|	6	|
|	STATEMENTS BY THE SPEAKER	|	2	|


So, by providing a JSON version of the Hansard dataset for over 10 years, we have a dataset that is **far** more usable than PDF or XML of individual sittings for several reasons:

1. Ease of Analysis and Processing:

* Structured Data: JSON is a structured data format, making it easy to parse and analyze with programming languages and data analysis tools. This allows for efficient searching, filtering, and extraction of specific information. PDFs and XML, while structured in their own ways, require more complex parsing and extraction techniques.
* Machine Readability: JSON is inherently machine-readable, allowing for automated analysis and integration with other datasets or applications. This enables researchers, journalists, and developers to extract insights and patterns from the Hansard data without manual intervention. PDFs and XML, while machine-readable to some extent, require more complex processing for machine understanding.

2. Data Integration and Interoperability:

* Standard Format: JSON is a widely adopted standard for data exchange, making it easily compatible with various systems and applications. This allows for seamless integration of the Hansard data with other datasets or tools for comprehensive analysis and research. PDFs and XML, while standard formats, require more complex transformations for integration with other datasets or tools.
* API Friendliness: JSON is commonly used in APIs, making it easy to access and interact with the Hansard data programmatically. This enables developers to build applications and services that leverage the Hansard data for various purposes. PDFs and XML, while accessible through APIs, require more complex handling for programmatic interaction.
3. Efficiency and Scalability:

* Compact Size: JSON is a relatively compact data format, making it efficient to store and transmit large volumes of data. This is crucial for a dataset spanning over 10 years of Hansard transcripts, which would be significantly larger in PDF or XML format.
Database Compatibility: JSON is natively supported by many modern databases, making it easy to store and manage the Hansard data for efficient querying and retrieval. PDFs and XML, while storable in databases, require more complex handling and indexing for efficient querying.

4. Accessibility and Openness:

* Text-Based: JSON is a text-based format, making it accessible to a wide range of users and tools. This promotes openness and transparency, enabling more people to access and analyze the Hansard data for various purposes. PDFs and XML, while accessible, require specific software or tools for viewing and analysis.

In summary: A JSON version of the Hansard dataset for over 10 years offers significant advantages in terms of ease of analysis, data integration, efficiency, and accessibility. It enables researchers, journalists, developers, and the public to leverage the vast amount of parliamentary information for various purposes, promoting transparency, accountability, and informed decision-making.

You can download the processed Hansard dataset [here](https://storage.googleapis.com/parlipulse-hansards/hansard_bq.ndjson.gz)

You can also access the individual XML files [here](https://console.cloud.google.com/storage/browser/parlipulse-hansards)
