package com.leoosato.project.resource;


import com.leoosato.project.dto.MatchupBreakdownResponse;
import com.leoosato.project.dto.MatchupRequest;
import com.leoosato.project.dto.MatchupResponse;
import com.leoosato.project.services.TeamAnalysisService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/api/v1/analyze/matchup")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MatchupResource {

    @Inject TeamAnalysisService svc;

    @POST
    @Path("/breakdown")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MatchupBreakdownResponse analyzeWithBreakdown(MatchupRequest req) {
        return svc.analyzeWithBreakdown(req.myTeam(), req.opponent());
    }
}
