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

```$xslt
#authorList	title	journal	year	volume	pages	pmid	inhouse	resource	clinical.use	phenogeno.algorithm	systems.bio.algorithm	hpo	monarch	common.disease	cross.species	environment	cancer
Köhler S, Vasilevsky NA, Engelstad M, Foster E, McMurry J, Aymé S, Baynam G, Bello SM, Boerkoel CF, Boycott KM, Brudno M, Buske OJ, Chinnery PF, Cipriani V, Connell LE, Dawkins HJ, DeMare LE, Devereau AD, de Vries BB, Firth HV, Freson K, Greene D, Hamosh A, Helbig I, Hum C, Jähn JA, James R, Krause R, F Laulederkind SJ, Lochmüller H, Lyon GJ, Ogishima S, Olry A, Ouwehand WH, Pontikos N, Rath A, Schaefer F, Scott RH, Segal M, Sergouniotis PI, Sever R, Smith CL, Straub V, Thompson R, Turner C, Turro E, Veltman MW, Vulliamy T, Yu J, von Ziegenweidt J, Zankl A, Züchner S, Zemojtel T, Jacobsen JO, Groza T, Smedley D, Mungall CJ, Haendel M, Robinson PN	The Human Phenotype Ontology in 2017	Nucleic Acids Res	2017	45(D1)	D865-D876	27899602	T	T	F	F	F	T	F	F	F	F	F
Fang H, Wu Y, Yang H, Yoon M, Jiménez-Barrón LT, Mittelman D, Robison R, Wang K, Lyon GJ	Whole genome sequencing of one complex pedigree illustrates challenges with genomic medicine	BMC Med Genomics	2017	10(1)	10	28228131	F	F	T	F	F	T	F	F	F	F	F
```