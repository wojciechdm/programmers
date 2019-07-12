package com.wojciechdm.programmers.company.structure.projectallocation;

import lombok.*;

import java.util.*;

@Getter
class ProjectAllocationValidationResult {

  private boolean isValid;
  private EnumMap<ProjectAllocationRuleViolation, Boolean> rulesViolations;

  private ProjectAllocationValidationResult(
      boolean isValid, EnumMap<ProjectAllocationRuleViolation, Boolean> rulesViolations) {
    this.isValid = isValid;
    this.rulesViolations = rulesViolations;
  }

  static ProjectAllocationValidationResult getInstance(
      EnumMap<ProjectAllocationRuleViolation, Boolean> rulesViolations) {
    return new ProjectAllocationValidationResult(
        !rulesViolations.containsValue(true), rulesViolations);
  }

  String extractOccurredRulesViolationsAsString() {
    StringBuilder violations = new StringBuilder();
    for (Map.Entry<ProjectAllocationRuleViolation, Boolean> ruleViolation :
        rulesViolations.entrySet()) {
      if (ruleViolation.getValue()) {
        violations.append(ruleViolation.getKey().getMessage()).append("; ");
      }
    }
    return violations.toString();
  }
}
