# pubman
Publication Manager

An app that helps to manage citations of HPO related articles.

![pubman GUI](src/main/resources/img/pubman.png?raw=true "PubMan")


To use PubMan, enter the PMID of an article that you want to add to the HPO/Monarch citation database. 
Choose one or more of the categories shown. If you choose ``HPO`` then the article will be shown on the
HPO website. If you choose ``Monarch`` the article will be marked as Monarch-specific but not necessarily related
to HPO. The other categories can be used to display certain classes of publications.

If the publication is correct, click the ``Add`` button to add it to the citation list.

If the publication has one of our team members as a coauthor, then we may want to include all publications that
cite this in our citation list. To do so, click the ``Retrieve citing`` button, which will utilize NCBI eUtils to
get a list of citing publications. The red line ``Number of articles in the stack waiting to be checked``
will show how many of these articles were found. Use the ``Show next`` button to retrieve data for the next publication
in the list, and add the publication to the citation database if appropriate.

Note that the app will keep duplicate entries from being entered and will show the duplication in the GUI (it is possibly a little annoying,
but the app shows this right after you enter an article for this first time to keep you from clicking too many times).

Here is the format of the file that the app produces. It is a simple tab separated format that
can be used to populate a simple database to show on our website etc.


The format is simple. The first six fields include all the information about the citation (authorList, title, journal, year, volume, pages, pmid).
The next is if ``inhouse`` and should be T (for true) if somebody from the Monarch team is a coauthor on the paper.
The next item is ``hpo`` and indicates that the topic is related in some way to HPO (it should be set to true for any article
that cites one of our in house papers). The next item is ``monarch``, which should be set to true for any article that
is related to Monarch but not to the HPO. The final item is ``topic.list``, and contains a list of the topics, in case
we ever want to offer that feature on our webpage (but which we can exploit with scripts for writing reports etc.)

```$xslt
#authorList	title	journal	year	volume	pages	pmid	inhouse	hpo	monarch	topic.list
Jia J, Wang R, An Z, Guo Y, Ni X, Shi T	RDAD: A Machine Learning System to Support Phenotype-Based Rare Disease Diagnosis	Front Genet	2018	9	587	30564269	F	T	F	phenogeno.algorithm
Dozmorov MG	Reforming disease classification system-are we there yet? Ann Transl Med	2018 Nov;6(Suppl 1):S30	2103	PubMed Central PMCID	 PMC6291543	30613605	F	T	F	review
Chen Y, Xu R	Context-sensitive network analysis identifies food metabolites associated with Alzheimer's disease: an exploratory study	BMC Med Genomics	2019	12(Suppl 1)	17	30704467	F	T	F	systems.bio.algorithm
Boudellioua I, Kulmanov M, Schofield PN, Gkoutos GV, Hoehndorf R	DeepPVP: phenotype-based prioritization of causative variants using deep learning	BMC Bioinformatics	2019	20(1)	65	30727941	F	T	F	phenogeno.algorithm
```


# HPO publications
Our goal in the first round is to gather citations of HPO related articles published by our group for display on the HPO website.
The following is a list of publications we have processed in this way.

*  Robinson PN, Köhler S, Bauer S, Seelow D, Horn D, Mundlos S. The Human Phenotype Ontology: a tool for annotating and analyzing human hereditary disease.
  Am J Hum Genet. 2008 Nov;83(5):610-5. PubMed PMID: 18950739
* Köhler S, Schulz MH, Krawitz P, Bauer S, Dölken S, Ott CE, Mundlos C, Horn D, Mundlos S, Robinson PN. Clinical diagnostics in human genetics with semantic
  similarity searches in ontologies. Am J Hum Genet. 2009 Oct;85(4):457-64. PubMed PMID: 19800049
* Robinson PN, Mundlos S. The human phenotype ontology. Clin Genet. 2010 Jun;77(6):525-34. PubMed PMID: 20412080.
* Gkoutos GV, Mungall C, Dolken S, Ashburner M, Lewis S, Hancock J, Schofield P, Kohler S, Robinson PN. Entity/quality-based logical definitions for the human
 skeletal phenome using PATO. Conf Proc IEEE Eng Med Biol Soc. 2009;2009:7069-72.  PubMed PMID: 19964203.
* Köhler S, Bauer S, Mungall CJ, Carletti G, Smith CL, Schofield P, Gkoutos GV,  Robinson PN. Improving ontologies by automatic reasoning and evaluation of
  logical definitions. BMC Bioinformatics. 2011 Oct 27;12:418. PubMed PMID: 22032770.
* Schulz MH, Köhler S, Bauer S, Robinson PN. Exact score distribution computation for ontological similarity searches. BMC Bioinformatics. 2011 Nov
  12;12:441. PubMed PMID: 22078312.
* Köhler S, Doelken SC, Rath A, Aymé S, Robinson PN. Ontological phenotype standards for neurogenetics. Hum Mutat. 2012;33(9):1333-9. PubMed PMID: 22573485.
* Bauer S, Köhler S, Schulz MH, Robinson PN. Bayesian ontology querying for accurate and noise-tolerant semantic searches. Bioinformatics. 2012 Oct
  1;28(19):2502-8. PubMed PMID: 22843981.
*  Köhler S, Doelken SC, Ruef BJ, Bauer S, Washington N, Westerfield M, Gkoutos  G, Schofield P, Smedley D, Lewis SE, Robinson PN, Mungall CJ. Construction and
  accessibility of a cross-species phenotype ontology along with gene annotations for biomedical research. Version 2. F1000Res. 2013 Feb 1 [revised 2014 Jan
  1];2:30. doi: 10.12688/f1000research.2-30.v2. eCollection 2013. PubMed PMID:24358873.
* Doelken SC, Köhler S, Mungall CJ, Gkoutos GV, Ruef BJ, Smith C, Smedley D, Bauer S, Klopocki E, Schofield PN, Westerfield M, Robinson PN, Lewis SE.
  Phenotypic overlap in the contribution of individual genes to CNV pathogenicity  revealed by cross-species computational analysis of single-gene mutations in
  humans, mice and zebrafish. Dis Model Mech. 2013 Mar;6(2):358-72. PubMed PMID: 23104991.
*  Köhler S, Doelken SC, Mungall CJ, Bauer S, Firth HV, Bailleul-Forestier I,
  Black GC, Brown DL, Brudno M, Campbell J, FitzPatrick DR, Eppig JT, Jackson AP,
  Freson K, Girdea M, Helbig I, Hurst JA, Jähn J, Jackson LG, Kelly AM, Ledbetter
  DH, Mansour S, Martin CL, Moss C, Mumford A, Ouwehand WH, Park SM, Riggs ER,
  Scott RH, Sisodiya S, Van Vooren S, Wapner RJ, Wilkie AO, Wright CF, Vulto-van
  Silfhout AT, de Leeuw N, de Vries BB, Washingthon NL, Smith CL, Westerfield M,
  Schofield P, Ruef BJ, Gkoutos GV, Haendel M, Smedley D, Lewis SE, Robinson PN.
  The Human Phenotype Ontology project: linking molecular biology and disease
  through phenotype data. Nucleic Acids Res. 2014 Jan;42(Database issue):D966-74.
  doi: 10.1093/nar/gkt1026. Epub 2013 Nov 11. PubMed PMID: 24217912; (Still needs more)