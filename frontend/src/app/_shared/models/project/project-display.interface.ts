import { ProjectStatus } from "./project-status.enum";

export interface ProjectDisplayInterface {
  id: number;
  name: string;
  codeName: string;
  startDate: string;
  endDate: string;
  status: ProjectStatus;
  createDate: string;
  lastModificationDate: string;
  clientId: number;
}
