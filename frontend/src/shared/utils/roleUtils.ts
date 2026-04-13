export const getRoleVariant = (role: string) => {
  switch (role) {
    case "CEO":
      return "danger";
    case "HR":
      return "warning";
    case "MANAGER":
      return "default";
    case "EMPLOYEE":
      return "gray";
    default:
      return "default";
  }
};