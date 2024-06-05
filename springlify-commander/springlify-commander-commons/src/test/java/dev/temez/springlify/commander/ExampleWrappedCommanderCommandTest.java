package dev.temez.springlify.commander;

import dev.temez.springlify.commander.annotation.CommandRoot;
import dev.temez.springlify.commander.annotation.CommanderCommand;
import dev.temez.springlify.commander.argument.adapter.ArgumentAdapterFactoryImpl;
import dev.temez.springlify.commander.argument.adapter.impl.IntegerArgumentAdapter;
import dev.temez.springlify.commander.argument.adapter.impl.StringArgumentAdapter;
import dev.temez.springlify.commander.command.CommandType;
import dev.temez.springlify.commander.command.Command;
import dev.temez.springlify.commander.command.completer.CommandCompleter;
import dev.temez.springlify.commander.command.completer.ProviderCommandCompleter;
import dev.temez.springlify.commander.command.completer.provider.impl.GenericCompletionProvider;
import dev.temez.springlify.commander.command.completer.provider.impl.SubCommandCompletionProvider;
import dev.temez.springlify.commander.command.discoverer.ClassBasedCommandDiscoverer;
import dev.temez.springlify.commander.command.discoverer.CommandDiscoverer;
import dev.temez.springlify.commander.command.discoverer.MethodBasedCommandDiscoverer;
import dev.temez.springlify.commander.command.invocation.CommandInvocation;
import dev.temez.springlify.commander.command.invocation.CommandInvocationImpl;
import dev.temez.springlify.commander.command.preprocessor.ExecutionPreprocessor;
import dev.temez.springlify.commander.command.preprocessor.SubcommandPreprocessor;
import dev.temez.springlify.commander.command.sender.Sender;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.mockito.Mockito.mock;

@SpringBootTest(classes = {
    StringArgumentAdapter.class,
    IntegerArgumentAdapter.class,
    ArgumentAdapterFactoryImpl.class,
    ProviderCommandCompleter.class,
    ClassBasedCommandDiscoverer.class,
    MethodBasedCommandDiscoverer.class,
    ExampleWrappedCommanderCommandTest.TestCommand.class,
    SubCommandCompletionProvider.class,
    GenericCompletionProvider.class,
    SubcommandPreprocessor.class
}
)
@TestPropertySource(
    properties = "logging.level.dev.temez.springlify=debug"
)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExampleWrappedCommanderCommandTest {

  @Autowired
  CommandDiscoverer<Class<?>> commandDiscoverer;

  @Autowired
  ExecutionPreprocessor executionPreprocessor;

  @Autowired
  CommandCompleter commandCompleter;

  @Test
  void tst() {
    Command command = commandDiscoverer.discover(TestCommand.class);
    CommandInvocation execution = new CommandInvocationImpl(
        mock(Sender.class),
        command,
        List.of("othersub", "sub", "so", "<")
    );

    executionPreprocessor.process(execution);
    System.out.println(commandCompleter.completeSorted(execution));
  }

  @CommanderCommand(
      name = "test",
      description = "commands.test.description",
      type = CommandType.SHARED
  )
  public static class TestCommand {

    @CommandRoot
    public void execute(@NotNull Sender<?> sender, @NotNull String arg) {

    }

    @CommanderCommand(
        name = "sub",
        description = "commands.test.sub.description"
    )
    public void executeSub(@NotNull Sender<?> sender, @NotNull Integer sosiPidor, @NotNull String AUEEE) {

    }

    @CommanderCommand(
        name = "othersub",
        description = "commands.test.othersub.description"
    )
    public static class OtherSubCommand {

      @CommandRoot
      public void execute(@NotNull Sender<?> sender, @NotNull String arg) {

      }

      @CommanderCommand(
          name = "sub",
          description = "commands.test.sub.description"
      )
      public void executeSub(@NotNull Sender<?> sender, @NotNull Integer sosiPidor, @NotNull String AUEEE) {

      }
    }
  }
}
