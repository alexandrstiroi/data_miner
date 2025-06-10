package org.shtiroy.data_miner.model;

import lombok.Data;
import java.util.List;

@Data
public class Period {
    private EnquiryPeriod enquiryPeriod;
    private TenderPeriod tenderPeriod;
    private List<Enquiry> enquiries;
}
