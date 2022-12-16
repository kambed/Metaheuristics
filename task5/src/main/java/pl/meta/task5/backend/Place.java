package pl.meta.task5.backend;

public class Place {
    private final int id;
    private final double x;
    private final double y;
    private final double demand;
    private final double readyTime;
    private final double dueTime;
    private final double serviceTime;

    public Place(int id, double x, double y, double demand, double readyTime, double dueTime, double serviceTime) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.demand = demand;
        this.readyTime = readyTime;
        this.dueTime = dueTime;
        this.serviceTime = serviceTime;
    }

    public int getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getDemand() {
        return demand;
    }

    public double getReadyTime() {
        return readyTime;
    }

    public double getDueTime() {
        return dueTime;
    }

    public double getServiceTime() {
        return serviceTime;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Place{");
        sb.append("id=").append(id);
        sb.append(", x=").append(x);
        sb.append(", y=").append(y);
        sb.append(", demand=").append(demand);
        sb.append(", readyTime=").append(readyTime);
        sb.append(", dueTime=").append(dueTime);
        sb.append(", serviceTime=").append(serviceTime);
        sb.append('}');
        return sb.toString();
    }
}
