package edu.iis.mto.testreactor.coffee;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import edu.iis.mto.testreactor.coffee.milkprovider.MilkProvider;
import edu.iis.mto.testreactor.coffee.milkprovider.MilkProviderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    private CoffeeReceipe coffeeReceipe;
    private CoffeeMachine coffeeMachine;
    private CoffeOrder coffeeOrder;

    @BeforeEach
    public void setUp() {
        coffeeMachine = new CoffeeMachine(grinder, milkProvider, coffeeReceipes);

        coffeeOrder = CoffeOrder.builder().withSize(CoffeeSize.STANDARD).withType(CoffeType.ESPRESSO).build();

        coffeeReceipe = CoffeeReceipe.builder().withWaterAmounts(Map.of(CoffeeSize.STANDARD, 100)).withMilkAmount(100).build();
    }

    @Test
    public void shouldReturnRightCoffeWithProperParams() {
        when(grinder.canGrindFor(CoffeeSize.STANDARD)).thenReturn(true);
        when(grinder.grind(any(CoffeeSize.class))).thenReturn(100d);
        when(coffeeReceipes.getReceipe(CoffeType.ESPRESSO)).thenReturn(Optional.of(coffeeReceipe));
        Coffee result = coffeeMachine.make(coffeeOrder);

        assertTrue(result.getCoffeeWeigthGr().equals(100d));
        assertTrue(result.getMilkAmout().equals(Optional.of(100)));
        assertTrue(result.getWaterAmount().equals(100));
    }

    @Test
    public void shouldUseProperMethodsWithProperParams() throws MilkProviderException {
        when(grinder.canGrindFor(CoffeeSize.STANDARD)).thenReturn(true);
        when(grinder.grind(any(CoffeeSize.class))).thenReturn(100d);
        when(coffeeReceipes.getReceipe(CoffeType.ESPRESSO)).thenReturn(Optional.of(coffeeReceipe));
        coffeeMachine.make(coffeeOrder);

        verify(grinder).grind(any());
        verify(milkProvider).heat();
        verify(milkProvider).pour(100);
        verify(coffeeReceipes, atLeast(1)).getReceipe(any());
    }

    @Test
    public void shouldThrowIfCantGrind() {
        when(grinder.canGrindFor(CoffeeSize.STANDARD)).thenReturn(false);

        assertThrows(NoCoffeeBeansException.class, () -> coffeeMachine.make(coffeeOrder));
    }

}
