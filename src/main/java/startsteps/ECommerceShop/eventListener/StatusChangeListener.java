package startsteps.ECommerceShop.eventListener;

import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.context.ApplicationListener;

public class StatusChangeListener implements ApplicationListener<AvailabilityChangeEvent> {

    @Override
    public void onApplicationEvent(AvailabilityChangeEvent event) {

    }
}
