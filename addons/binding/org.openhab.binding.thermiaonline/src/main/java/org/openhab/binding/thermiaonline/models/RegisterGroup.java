package org.openhab.binding.thermiaonline.models;

import java.util.ArrayList;
import java.util.List;

public class RegisterGroup {
    public String groupName;
    public String groupDefinition;
    public List<Register> registers = new ArrayList<Register>();
}
