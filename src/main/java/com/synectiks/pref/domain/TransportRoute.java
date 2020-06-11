package com.synectiks.pref.domain;


import com.synectiks.pref.domain.enumeration.RouteFrequency;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A TransportRoute.
 */
@Entity
@Table(name = "transport_route")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
//@Document(indexName = "transportroute")
public class TransportRoute implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "route_name")
    private String routeName;

    @Column(name = "route_details")
    private String routeDetails;

    @Column(name = "route_map_url")
    private String routeMapUrl;

    @Column(name = "no_of_stops")
    private Integer noOfStops;

    @Enumerated(EnumType.STRING)
    @Column(name = "route_frequency")
    private RouteFrequency routeFrequency;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRouteName() {
        return routeName;
    }

    public TransportRoute routeName(String routeName) {
        this.routeName = routeName;
        return this;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getRouteDetails() {
        return routeDetails;
    }

    public TransportRoute routeDetails(String routeDetails) {
        this.routeDetails = routeDetails;
        return this;
    }

    public void setRouteDetails(String routeDetails) {
        this.routeDetails = routeDetails;
    }

    public String getRouteMapUrl() {
        return routeMapUrl;
    }

    public TransportRoute routeMapUrl(String routeMapUrl) {
        this.routeMapUrl = routeMapUrl;
        return this;
    }

    public void setRouteMapUrl(String routeMapUrl) {
        this.routeMapUrl = routeMapUrl;
    }

    public Integer getNoOfStops() {
        return noOfStops;
    }

    public TransportRoute noOfStops(Integer noOfStops) {
        this.noOfStops = noOfStops;
        return this;
    }

    public void setNoOfStops(Integer noOfStops) {
        this.noOfStops = noOfStops;
    }

    public RouteFrequency getRouteFrequency() {
        return routeFrequency;
    }

    public TransportRoute routeFrequency(RouteFrequency routeFrequency) {
        this.routeFrequency = routeFrequency;
        return this;
    }

    public void setRouteFrequency(RouteFrequency routeFrequency) {
        this.routeFrequency = routeFrequency;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TransportRoute transportRoute = (TransportRoute) o;
        if (transportRoute.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), transportRoute.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TransportRoute{" +
            "id=" + getId() +
            ", routeName='" + getRouteName() + "'" +
            ", routeDetails='" + getRouteDetails() + "'" +
            ", routeMapUrl='" + getRouteMapUrl() + "'" +
            ", noOfStops=" + getNoOfStops() +
            ", routeFrequency='" + getRouteFrequency() + "'" +
            "}";
    }
}
