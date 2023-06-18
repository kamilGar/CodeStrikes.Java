package net.codestrikes;

import net.codestrikes.sdk.*;

import java.util.*;

public class PlayerBot extends BotBase {

    private Area attack1 = Area.HookKick;
    private Area attack2 = Area.UppercutPunch;
    private Area defence = Area.HookKick;
    private List<RoundContext> opponentsMoves = new ArrayList<RoundContext>();

    private Area ChangeAttack() {

        int countHookKicks = 0;
        int countHookPunches = 0;
        int countUppercutPunches = 0;
        int countLowKicks = 0;

        for (RoundContext move : opponentsMoves) {
            if (move.getLastOpponentMoves() != null) {
                if (Arrays.stream(move.getLastOpponentMoves().getDefences()).anyMatch(x -> x.getArea() == Area.HookKick)) {
                    countHookKicks++;
                }
                if (Arrays.stream(move.getLastOpponentMoves().getDefences()).anyMatch(x -> x.getArea() == Area.HookPunch)) {
                    countHookPunches++;
                }
                if (Arrays.stream(move.getLastOpponentMoves().getDefences()).anyMatch(x -> x.getArea() == Area.UppercutPunch)) {
                    countUppercutPunches++;
                }
                if (Arrays.stream(move.getLastOpponentMoves().getDefences()).anyMatch(x -> x.getArea() == Area.LowKick)) {
                    countLowKicks++;
                }
            }
        }

        int TheRarestKick = Math.min(countHookKicks, countLowKicks);
        int TheRarestPunch = Math.min(countHookPunches, countUppercutPunches);

        if (TheRarestKick < TheRarestPunch) {
            if (TheRarestKick == countHookKicks) {
                return Area.HookKick;
            } else {
                return Area.LowKick;
            }
        } else {
            if (TheRarestPunch == countHookPunches) {
                return Area.HookPunch;
            } else {
                return Area.UppercutPunch;
            }
        }
    }

    private Area ChangeDefence() {

        int countHookKicks = 0;
        int countHookPunches = 0;
        int countUppercutPunches = 0;
        int countLowKicks = 0;

        for (RoundContext move : opponentsMoves) {
            if (move.getLastOpponentMoves() != null) {
                if (Arrays.stream(move.getLastOpponentMoves().getAttacks()).anyMatch(x -> x.getArea() == Area.HookKick)) {
                    countHookKicks++;
                }
                if (Arrays.stream(move.getLastOpponentMoves().getAttacks()).anyMatch(x -> x.getArea() == Area.HookPunch)) {
                    countHookPunches++;
                }
                if (Arrays.stream(move.getLastOpponentMoves().getAttacks()).anyMatch(x -> x.getArea() == Area.UppercutPunch)) {
                    countUppercutPunches++;
                }
                if (Arrays.stream(move.getLastOpponentMoves().getAttacks()).anyMatch(x -> x.getArea() == Area.LowKick)) {
                    countLowKicks++;
                }
            }
        }

        int TheMostCommonKick = Math.max(countHookKicks, countLowKicks);
        int TheMostCommonPunch = Math.max(countHookPunches, countUppercutPunches);

        if (new Random().nextDouble() > 0.7d) {
            if (TheMostCommonKick == countHookKicks) {
                return Area.HookKick;
            } else {
                return Area.LowKick;
            }
        } else {
            if (TheMostCommonPunch == countHookPunches) {
                return Area.HookPunch;
            } else {
                return Area.UppercutPunch;
            }
        }
    }


    public MoveCollection nextMove(RoundContext context) {

        opponentsMoves.add(context);
        defence = ChangeDefence();
        attack1 = ChangeAttack();

        if (context.getMyDamage() < context.getOpponentDamage()) {
            context.getMyMoves()
                    .addAttack(attack2)
                    .addAttack(attack2)
                    .addDefence(Area.HookKick);
            if (defence != Area.HookKick) {
                context.getMyMoves()
                        .addDefence(defence);
            } else {
                context.getMyMoves()
                        .addDefence(Area.HookPunch);
            }

        } else {
            context.getMyMoves()
                    .addAttack(Area.HookPunch)
                    .addAttack(Area.LowKick)
                    .addAttack(attack1);
            if (attack1 == Area.HookPunch) {
                context.getMyMoves()
                        .addAttack(Area.LowKick);
            } else if (attack1 == Area.UppercutPunch) {
                context.getMyMoves()
                        .addAttack(Area.UppercutPunch);
            } else if (attack1 == Area.LowKick) {
                context.getMyMoves()
                        .addAttack(Area.HookPunch);
            }

            if (new Random().nextDouble() > 0.15d) {
                if (new Random().nextDouble() > 0.25d) {
                    context.getMyMoves().addAttack(Area.HookKick);
                } else {
                    context.getMyMoves().addAttack(attack2);
                    context.getMyMoves().addAttack(attack2);
                }
            } else {
                context.getMyMoves().addDefence(defence);
            }

        }

        return context.getMyMoves();

    }

    @Override
    public String toString() {
        return "PlayerBot";
    }
}