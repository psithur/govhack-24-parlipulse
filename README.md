# Parli Pulse

## [GovHack 2024](https://www.govhack.org)

This GovHack project leverages Large Language Models (AI) to analyze parliamentary transcripts, 
identifying and highlighting instances of constructive (and divisive) disagreement among politicians. 

By providing this analysis, the project aims to promote healthier debate practices and ultimately 
enhance democracy by fostering a more collaborative and productive political environment.

It also aims to raise the level of public discourse around usage of AI by providing all the data and
details of how the data is used and combined with LLM prompts to produce an interesting, and hopefully
constructive result.

[Goto the interactive dashboard](https://lookerstudio.google.com/reporting/f6904783-fecd-4b04-8039-9c76e481e1b9)

![ParliPulse logo](ParliPulse.png)

![ParliPulse dashboard](dash.jpg)



## Data Sources

### [Federal Parliament - House of Reps - Official Hansard](https://www.aph.gov.au/Parliamentary_Business/Hansard)

Description: The Federal Government Hansard is the official, edited transcript of the proceedings of the Australian Parliament. It records debates, speeches, questions, and other parliamentary business

Note: I have focussed on just the House of Reps due to time constraints

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


A big motivation for me in participating in GovHack is to make public datasets more accessable, so as part of this 
challenge I am providing a full copy of the JSON version of the Hansard dataset for over 10 years.

This means everyone has a dataset that is **far** more usable than PDF or XML of individual sittings for several reasons:

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


## Using AI to enrich the dataset

After getting the data into a format that was more usable for reuse and "Hacking", my next step was to figure out how
to use a Large Language Model to enrich the unstructured dataset - politicians speeches in parliament.

I decided to use Google's platform and the Gemini Pro 1.5 model and given the time constraints, I needed to analyse
thousands of free text speeches as quickly as possible.

I started by [uploading my dataset into BigQuery](https://cloud.google.com/bigquery/docs/loading-data-cloud-storage-json#loading_json_data_into_a_new_table), 
and then following [the instructions](https://cloud.google.com/bigquery/docs/generate-text) to connect BigQuery
with [Gemini Pro 1.5](https://cloud.google.com/bigquery/docs/reference/standard-sql/bigqueryml-syntax-create-remote-model#endpoint)

If I had more time, I would have explored other available models to ascertain whether a smaller (cheaper) model
would provide comparable results.

Once I had BigQuery and Gemini talking, the next step was to iterate through a series of LLM Prompts to find
the right instructions for the model to classify the data.

As I was working in the GovHack Hackerspace in Canberra while working on this element of my entry, I was able to 
crowd source improvements to the prompt over an hour or two. Thanks to those mentors who helped!

Here's an example of testing a prompt on 10 random speeches.

```sql
CREATE TABLE `govhack-24-parlipulse.federal_hansard.first_output` as
SELECT *
FROM ML.GENERATE_TEXT(
  MODEL `govhack-24-parlipulse.federal_hansard.g15pro`,
      (
      SELECT CONCAT(
        """You are a analyst at a think tank that studies deliberative Democracy and strengthening global governance. 
        Analyze the following transcript of a political speech in parliament and classify its overall tone and content on a scale of 1 to 5 (RATING) where:

* **1** represents a **very polite, constructive, and collaborative** speech.
* **5** represents a **very impolite, divisive, and unconstructive** speech.

Consider the following factors in your analysis:

* **Language and Tone:** Assess the use of respectful language, the presence of personal attacks or inflammatory rhetoric, and the overall tone of the speech (e.g., conciliatory vs. antagonistic).
* **Focus on Issues:** Evaluate whether the speech primarily focuses on addressing policy issues and presenting solutions or if it's centered on criticizing opponents and creating division.
* **Collaboration and Compromise:** Determine if the speech demonstrates a willingness to collaborate with others, find common ground, and seek compromise, or if it adopts a rigid and uncompromising stance.
* **Respect for Others:** Consider whether the speech shows respect for differing viewpoints and acknowledges the contributions of others, or if it dismisses opposing perspectives and seeks to undermine them.

Provide specific examples from the transcript to support your classification in the REASONING field. 
Summarize the key subjects or issues being discussed, in the SUBJECTS field 
Identify the speakers primary position on the key subjects, whether they Supports or Opposes in the POSITION field.

Your response should be a JSON object without backticks or anything else, in the following form

{
 "RATING":YOUR_RATING, #Number between 1 and 5
 "SUBJECTS":SUBJECTS_DISCUSSED, #Top 3, each in no more than one to two words, prioritising specific policy proposals if relevant (e.g. Climate; Taxation; Migration)
 "POSITION":SPEAKERS_POSITION_ON_SUBJECT, #One of the following, with no other content: "Supports; Opposes; Other"
 "REASONING":REASONING #Free text, no more than 200 words
 }

Input transcript:
"""
      , text) AS prompt, speaker_party, speaker_electorate, info_title, date, speaker_name,
 CHAR_LENGTH(text) AS character_count

      FROM federal_hansard.import
      WHERE date >= DATE_SUB(CURRENT_DATE(), INTERVAL 12 MONTH)
and info_title IN ('BILLS', 'STATEMENTS BY MEMBERS', 'MATTERS OF PUBLIC IMPORTANCE', 'MOTIONS', "PRIVATE MEMBERS' BUSINESS", "QUESTIONS WITHOUT NOTICE: ADDITIONAL ANSWERS")

    ),
  
  STRUCT(4096 AS max_output_tokens, 0 AS temperature,
  0.95 AS top_p, true AS flatten_json_output,
  false AS ground_with_google_search)
);
```

The prompt above is the last prompt - the one that I used to produce the output dataset below 
and that drives the interactive dashboard. 

My initial attempt to run this prompt over all the data timed out after 6 hours..., so instead I decided to run
it over the last 12 months (to 7 Sept 2024) resulting in over 4,000 enriched records.

This took almost 4 hours to run (I did not have any time to look at optimising this part of my analysis unfortunately).

You can download this dataset [here](https://storage.googleapis.com/parlipulse-hansards/ai_enriched_hansard_12_months_to_sept24.ndjson.gz) 
or explore the data using an interactive dashboard [here](https://lookerstudio.google.com/reporting/f6904783-fecd-4b04-8039-9c76e481e1b9)

I would love to extend this analysis further in the future.
