# 📦 CommandAPI - 동적 명령어 & 비동기 처리 지원 Bukkit API

CommandAPI는 마인크래프트 Paper 1.21+ 서버에서 사용할 수 있는 **동적 명령어 등록 및 자동완성/비동기 실행 지원 API**입니다. 플러그인 개발자가 `plugin.yml` 없이도 명령어를 등록할 수 있으며, 자동완성과 비동기 처리를 통해 **TPS 유지**와 **유저 경험 개선**을 동시에 달성할 수 있습니다.

---

## 🔧 설치 방법

### 1. 코드 포함
CommandAPI는 독립형 플러그인이 아니라, **당신의 플러그인 내부에 직접 포함하여 사용하는 구조**입니다.

`org.com.command` 패키지를 복사하여 프로젝트에 추가하세요.

---

## ✨ 주요 기능

| 기능 | 설명 |
|------|------|
| ✅ 동적 명령어 등록 | 서버 실행 중 명령어를 추가/삭제할 수 있음 |
| ⚡ 비동기 명령어 실행 | DB 쿼리 등 무거운 로직을 메인 스레드에 부담 없이 실행 가능 |
| 📋 자동완성(Tab Completion) | args 기반 커스텀 자동완성 지원 |
| 🔐 권한 설정 & 오피 제한 | 명령어에 permission 설정 가능, 오피 전용 명령어도 지정 가능 |
| 🔁 명령어 reload/remove/list | 전체 명령어 관리 지원 |

---

## 📌 사용 예시

```java
// 명령어 등록
CommandRegistry registry = Loader.getInstance().getCommandRegistry();

registry.registerCommand("내정보", (sender, args) -> {
    Player player = (Player) sender;
    player.sendMessage("[정보] 닉네임: " + player.getName());
}, false, "myplugin.info");

// 오피 전용 명령어 (permission: "op")
registry.registerCommand("서버초기화", (sender, args) -> {
    if (!(sender instanceof Player player) || !player.isOp()) return;
    sender.sendMessage("⚠️ 서버 초기화를 시작합니다...");
}, false, "op");

// 자동완성 + 비동기 실행 예시
registry.registerCommand(
    "골드보내기",
    (sender, args) -> {
        String target = args[0];
        int amount = Integer.parseInt(args[1]);
        // DB 처리 비동기 실행 (async = true)
    },
    true,
    null,
    (sender, args) -> {
        if (args.length == 1) {
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
        }
        return List.of();
    }
);

// 명령어 제거
registry.unregisterCommand("골드보내기");

// 명령어 갱신 (기존 명령어 제거 후 새로 등록)
registry.reloadCommand("내정보", (s, a) -> s.sendMessage("정보 다시 불러옴!"), false, "myplugin.info");

// 모든 등록된 명령어 제거
registry.clearCommands();

// 등록된 명령어 목록 확인
registry.listCommands(sender);
```

---

## 📂 서브 명령어 패턴 예시

```java
// /골드 [순위|보내기|꺼내기] 등 다양한 하위 명령어를 분기 처리하는 구조
registry.registerCommand("골드", goldCommand::handleGoldCommand, false, null);

// GoldCommand 클래스 내부에서
public void handleGoldCommand(CommandSender sender, String[] args) {
    if (!(sender instanceof Player player)) {
        sender.sendMessage("플레이어만 사용 가능합니다.");
        return;
    }

    if (args.length == 0) {
        player.sendMessage("/골드 [순위|보내기|꺼내기]");
        return;
    }

    switch (args[0]) {
        case "순위" -> openRank(player);
        case "보내기" -> sendGold(player, args);
        case "꺼내기" -> withdrawGold(player, args);
        default -> player.sendMessage("올바른 명령어를 입력하세요.");
    }
}
```

이 구조는 `/골드`, `/파티`, `/직업` 등의 복잡한 명령어를 단일 진입점으로 처리할 때 유용합니다.

---

## 📁 구성 구조

```
org.com.command
├── Loader.java            // 플러그인 진입점 (CommandRegistry 관리)
├── CommandRegistry.java   // 동적 명령어 등록/해제/리로드/목록 API
├── CommandHandler.java    // 실제 명령어 로직 실행, 탭완성 포함
├── AsyncCommandExecutor.java // 비동기 실행 처리기
├── CustomCommand.java     // 람다 기반 명령어 구현 인터페이스
└── CustomTabCompleter.java // 람다 기반 자동완성 인터페이스
```

---

## 📦 의존성
- PaperMC 1.21+
- Java 17+

---

## 🧩 확장 아이디어
- ✅ 서브 명령어 체계 (`/골드 보내기`, `/골드 순위` 등)
- ✅ 주석 기반 명령어 등록 (@Command 어노테이션 등)
- ✅ GUI와 연계한 명령어 처리 시스템 (예: /워프 GUI)

---

## 🧑‍💻 제작자
- 개발자: 강
- GitHub: (링크 예정)

---

## 📜 라이선스
- 이 코드는 자유롭게 수정/재배포할 수 있습니다. 단, 상업적 용도 시 제작자 표기를 부탁드립니다.
- 별로다 싶으면 브랜치 생성 및 수정 후 풀리퀘스트 ><

