package org.monarchinitative.item;


import org.junit.jupiter.api.Test;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static junit.framework.TestCase.assertTrue;

public class RecordParserTest {

    private static String record = "<?xmlversion=\"1.0\"encoding=\"UTF-8\"?><!DOCTYPEeLinkResultPUBLIC\"-//NLM//DTDelink20101123//EN\"\"https://eutils.ncbi.nlm.nih.gov/eutils/dtd/20101123/elink.dtd\"><eLinkResult><LinkSet><DbFrom>pubmed</DbFrom><IdList><Id>27899602</Id></IdList><LinkSetDb><DbTo>pubmed</DbTo><LinkName>pubmed_pubmed_citedin</LinkName><Link><Id>30626441</Id></Link><Link><Id>30616557</Id></Link><Link><Id>30597900</Id></Link><Link><Id>30576485</Id></Link><Link><Id>30559314</Id></Link><Link><Id>30559311</Id></Link><Link><Id>30535356</Id></Link><Link><Id>30532623</Id></Link><Link><Id>30477555</Id></Link><Link><Id>30476213</Id></Link><Link><Id>30462303</Id></Link><Link><Id>30445645</Id></Link><Link><Id>30423077</Id></Link><Link><Id>30423068</Id></Link><Link><Id>30420816</Id></Link><Link><Id>30418591</Id></Link><Link><Id>30407550</Id></Link><Link><Id>30404926</Id></Link><Link><Id>30400963</Id></Link><Link><Id>30393454</Id></Link><Link><Id>30381343</Id></Link><Link><Id>30380087</Id></Link><Link><Id>30346593</Id></Link><Link><Id>30339214</Id></Link><Link><Id>30311381</Id></Link><Link><Id>30311371</Id></Link><Link><Id>30286784</Id></Link><Link><Id>30279426</Id></Link><Link><Id>30255817</Id></Link><Link><Id>30248891</Id></Link><Link><Id>30240502</Id></Link><Link><Id>30239781</Id></Link><Link><Id>30238501</Id></Link><Link><Id>30224793</Id></Link><Link><Id>30190574</Id></Link><Link><Id>30173820</Id></Link><Link><Id>30132229</Id></Link><Link><Id>30131872</Id></Link><Link><Id>30110963</Id></Link><Link><Id>30107533</Id></Link><Link><Id>30100824</Id></Link><Link><Id>30083448</Id></Link><Link><Id>30062216</Id></Link><Link><Id>30057031</Id></Link><Link><Id>30002402</Id></Link><Link><Id>29997612</Id></Link><Link><Id>29980210</Id></Link><Link><Id>29974258</Id></Link><Link><Id>29961570</Id></Link><Link><Id>29946186</Id></Link><Link><Id>29934635</Id></Link><Link><Id>29888054</Id></Link><Link><Id>29884787</Id></Link><Link><Id>29790234</Id></Link><Link><Id>29745853</Id></Link><Link><Id>29700074</Id></Link><Link><Id>29688377</Id></Link><Link><Id>29659566</Id></Link><Link><Id>29632381</Id></Link><Link><Id>29603867</Id></Link><Link><Id>29600497</Id></Link><Link><Id>29590633</Id></Link><Link><Id>29590301</Id></Link><Link><Id>29572503</Id></Link><Link><Id>29562236</Id></Link><Link><Id>29549119</Id></Link><Link><Id>29543799</Id></Link><Link><Id>29514101</Id></Link><Link><Id>29487416</Id></Link><Link><Id>29481901</Id></Link><Link><Id>29444904</Id></Link><Link><Id>29437798</Id></Link><Link><Id>29437776</Id></Link><Link><Id>29403392</Id></Link><Link><Id>29397366</Id></Link><Link><Id>29396563</Id></Link><Link><Id>29374474</Id></Link><Link><Id>29373609</Id></Link><Link><Id>29370760</Id></Link><Link><Id>29340838</Id></Link><Link><Id>29322915</Id></Link><Link><Id>29311971</Id></Link><Link><Id>29259393</Id></Link><Link><Id>29254434</Id></Link><Link><Id>29250549</Id></Link><Link><Id>29243736</Id></Link><Link><Id>29240891</Id></Link><Link><Id>29227115</Id></Link><Link><Id>29207981</Id></Link><Link><Id>29197409</Id></Link><Link><Id>29186510</Id></Link><Link><Id>29181379</Id></Link><Link><Id>29165669</Id></Link><Link><Id>29158551</Id></Link><Link><Id>29126123</Id></Link><Link><Id>29112736</Id></Link><Link><Id>29106618</Id></Link><Link><Id>29092072</Id></Link><Link><Id>29089047</Id></Link><Link><Id>29063562</Id></Link><Link><Id>29025394</Id></Link><Link><Id>28951502</Id></Link><Link><Id>28946786</Id></Link><Link><Id>28881973</Id></Link><Link><Id>28864462</Id></Link><Link><Id>28838066</Id></Link><Link><Id>28804138</Id></Link><Link><Id>28796445</Id></Link><Link><Id>28701297</Id></Link><Link><Id>28700664</Id></Link><Link><Id>28699299</Id></Link><Link><Id>28650483</Id></Link><Link><Id>28603714</Id></Link><Link><Id>28550066</Id></Link><Link><Id>28545339</Id></Link><Link><Id>28535294</Id></Link><Link><Id>28532469</Id></Link><Link><Id>28529714</Id></Link><Link><Id>28475856</Id></Link><Link><Id>28388656</Id></Link><Link><Id>28387809</Id></Link><Link><Id>28228131</Id></Link></LinkSetDb></LinkSet></eLinkResult>";


    @Test
    void testRegex1() {
        Pattern pat = Pattern.compile("<Id>(\\d+)</Id>");
        Matcher mat = pat.matcher(record);
        assertTrue(mat.find());
    }



}
