package edu.iis.mto.testreactor.coffee;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import edu.iis.mto.testreactor.coffee.milkprovider.MilkProvider;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CoffeeMachineTest {

    @Mock
    private Grinder grinder;
    @Mock
    private MilkProvider milkProvider;
    @Mock
    private CoffeeReceipes coffeeReceipes;

    private CoffeeMachine coffeeMachine;
    private CoffeOrder coffeeOrder;
    private CoffeeReceipe coffeeReceipe;
    private Coffee properCoffee;

    @BeforeEach
    public void setUp() {
        coffeeMachine = new CoffeeMachine(grinder, milkProvider, coffeeReceipes);

        coffeeOrder = CoffeOrder
                .builder().withSize(CoffeeSize.SMALL)
                .withType(CoffeType.ESPRESSO)
                .build();

        properCoffee = new Coffee();

        coffeeReceipe = CoffeeReceipe
                .builder()
                .withMilkAmount(100)
                .withWaterAmounts(new HashMap< CoffeeSize, Integer > ())
                .build();

        when(grinder.canGrindFor(any())).thenReturn(true);
        when(coffeeReceipes.getReceipe(CoffeType.ESPRESSO)).thenReturn(Optional.of(coffeeReceipe));
    }

    @Test
    public void shouldReturnRightCoffeWithProperParams() {
        Coffee result = coffeeMachine.make(coffeeOrder);

        assertTrue(result.getCoffeeWeigthGr().equals(CoffeeSize.STANDARD));
    }

}
