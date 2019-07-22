package org.monarchinitiative.pubman.item;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Convenience class to check that the fields of an {@link Item} make sense
 */
public class ItemQC {

    private final Date oldestAllowableDate;
    private final Date today;
    private final SimpleDateFormat formatter = new SimpleDateFormat("dd/mm/yyyy");


    public ItemQC() throws ParseException  {

        oldestAllowableDate = formatter.parse("01/01/2008");
        Calendar cal = Calendar.getInstance();
        today = cal.getTime();

    }

    public boolean check(Item item) {
        String year = item.getEntry().getYear();
        String fakestr = String.format("01/01/%s",year);
        try {
            Date fakedate = formatter.parse(fakestr);
            if (fakedate.after(today) || fakedate.before(oldestAllowableDate)) {
                return false; // Date outside allowable range -- probably a parse error
            }
        } catch(ParseException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
