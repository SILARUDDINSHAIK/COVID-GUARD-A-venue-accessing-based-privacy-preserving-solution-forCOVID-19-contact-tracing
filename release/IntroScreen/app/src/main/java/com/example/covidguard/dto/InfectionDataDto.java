package com.example.covidguard.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class InfectionDataDto
{
    String uuid;
    List<Map<String, Date>> times;

    public InfectionDataDto() {
    }

    public InfectionDataDto(String uuid, List<Map<String, Date>> times) {
        this.uuid = uuid;
        this.times = times;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<Map<String, Date>> getTimes() {
        return times;
    }

    public void setTimes(List<Map<String, Date>> times) {
        this.times = times;
    }

    private String printTimes(){
        StringBuilder sb = new StringBuilder("[");
        for (Map<String, Date> time : times)
        {
            sb.append("start time=" + time.get("startTime") + ",");
            sb.append("end time=" + time.get("endTime") + "\n");
        }
        sb.append("]");
        return sb.toString();
    }
    @Override
    public String toString() {
        return "InfectionDataDto{" +
                "uuid='" + uuid + '\'' +
                ", times=" + printTimes() +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InfectionDataDto)) return false;
        InfectionDataDto that = (InfectionDataDto) o;
        return uuid.equals(that.uuid);
    }
    @Override
    public int hashCode()
    {
        return Objects.hash(uuid);
    }
}
