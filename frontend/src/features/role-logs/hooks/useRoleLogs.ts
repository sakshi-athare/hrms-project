import { keepPreviousData, useQuery } from "@tanstack/react-query"
import { getRoleLogs, getRoleLogsByUser } from "../api/roleLogApi"
import { useId } from "react";

export const useRoleLogs = (page: number, limit: number) => {
  return useQuery({
    queryKey: ["roleLogs", page, limit],
    queryFn: () => getRoleLogs(page, limit),
   placeholderData: keepPreviousData,
  });
};


export const useUserRoleLogs = (userId : number)=>{
   return useQuery({
    queryKey:["userRoleLogs",useId],
    queryFn: () => getRoleLogsByUser(userId),
    enabled:!!userId,
   });
};