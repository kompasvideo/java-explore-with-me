package ru.practicum.explorewithme.statmodule;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class StatModule {
    private final ClientStat clientStat;

    public StatModule(ClientStat clientStat) {
        this.clientStat = clientStat;
    }

    public void getAll(HttpServletRequest request) {
        clientStat.post("GET EVENTS", request.getRemoteAddr(), request.getRequestURI());
    }
}
