class BioMonitor {
    private int hp;
    private final int maxHp;
    private int humanity;
    private boolean cyberpsychosis;

    public BioMonitor(int maxHp) {
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.humanity = 100;
        this.cyberpsychosis = false;
    }

    public int getHp() {
        return hp;
    }

    public int getHumanity() {
        return humanity;
    }

    public boolean isCyberpsychosis() {
        return cyberpsychosis;
    }

    public void reduceHumanity(int cost) {
        humanity -= cost;
        if (humanity < 0) {
            humanity = 0;
        }
        if (humanity < 20) {
            cyberpsychosis = true;
        }
    }

    public void injectImmunoblockers(int restoreAmount) {
        humanity += restoreAmount;
        if (humanity > 100) {
            humanity = 100;
        }
        if (humanity >= 20) {
            cyberpsychosis = false;
        }
    }

    public int applyDamage(int rawDamage, int totalArmor) {
        int damageTaken = Math.max(5, rawDamage - totalArmor);
        hp -= damageTaken;
        if (hp < 0) {
            hp = 0;
        }
        return damageTaken;
    }

    public void applyOverdriveDamage() {
        int selfDamage = (int) (maxHp * 0.1);
        hp -= selfDamage;
        if (hp < 0) {
            hp = 0;
        }
    }

    @Override
    public String toString() {
        int totalBars = 10;
        int filledBars = (int) (((double) hp / maxHp) * totalBars);
        if (filledBars == 0 && hp > 0) {
            filledBars = 1;
        }

        String hpBar = "";
        for (int i = 0; i < totalBars; i++) {
            if (i < filledBars) {
                hpBar += "█";
            } else {
                hpBar += "░";
            }
        }

        String status = cyberpsychosis ? " [🔥 CYBERPSYCHOSIS]" : " (Стабільний)";
        return "HP [" + hpBar + "] " + hp + "/" + maxHp + " | Humanity: " + humanity + "/100" + status;
    }
}

abstract class AbstractCyberImplant {
    private final String name;
    private final String slot;
    private final int humanityCost;

    public AbstractCyberImplant(String name, String slot, int humanityCost) {
        this.name = name;
        this.slot = slot;
        this.humanityCost = humanityCost;
    }

    public String getName() {
        return name;
    }

    public String getSlot() {
        return slot;
    }

    public int getHumanityCost() {
        return humanityCost;
    }

    public abstract int getAttackBonus();
    public abstract int getArmorBonus();
    public abstract String activateSpecialAbility();

    @Override
    public String toString() {
        return "[" + slot + "] " + name + " (+" + getAttackBonus() + " ATK, +" + getArmorBonus() + " DEF, -" + humanityCost + " Hum)";
    }
}

class SandevistanOs extends AbstractCyberImplant {
    public SandevistanOs() {
        super("Militech Sandevistan Apogee", "Нейро-ОС", 35);
    }

    @Override
    public int getAttackBonus() {
        return 90;
    }

    @Override
    public int getArmorBonus() {
        return 20;
    }

    @Override
    public String activateSpecialAbility() {
        return "⚡ САНДЕВІСТАН АКТИВОВАНО: Час сповілено на 85%! Вороги завмерли на місці!";
    }
}

class MantisBlades extends AbstractCyberImplant {
    public MantisBlades() {
        super("Термальні Клинки Богомола", "Руки", 25);
    }

    @Override
    public int getAttackBonus() {
        return 110;
    }

    @Override
    public int getArmorBonus() {
        return 5;
    }

    @Override
    public String activateSpecialAbility() {
        return "🔥 Вихор розпечених клинків Богомола розтинає броню ворога!";
    }
}

class SubdermalTitaniumArmor extends AbstractCyberImplant {
    public SubdermalTitaniumArmor() {
        super("Підшкірна Титанова Броня", "Шкіра", 25);
    }

    @Override
    public int getAttackBonus() {
        return 10;
    }

    @Override
    public int getArmorBonus() {
        return 65;
    }

    @Override
    public String activateSpecialAbility() {
        return "🛡️ Нано-пластини поглинають кінетичний удар!";
    }
}

class CyberMercenary {
    private final String nickname;
    private final int baseAttack;
    private final BioMonitor bioMonitor;
    private final AbstractCyberImplant[] implants;
    private int implantCount;

    public CyberMercenary(String nickname, int baseAttack, int maxHp) {
        this.nickname = nickname;
        this.baseAttack = baseAttack;
        this.bioMonitor = new BioMonitor(maxHp);
        this.implants = new AbstractCyberImplant[4];
        this.implantCount = 0;
    }

    public BioMonitor getBioMonitor() {
        return bioMonitor;
    }

    public boolean installImplant(AbstractCyberImplant implant) {
        for (int i = 0; i < implantCount; i++) {
            if (implants[i].getSlot().equals(implant.getSlot())) {
                System.out.println("❌ Помилка слота: Слот [" + implant.getSlot() + "] вже зайнятий імплантом '" + implants[i].getName() + "'!");
                return false;
            }
        }

        if (implantCount >= implants.length) {
            return false;
        }

        implants[implantCount] = implant;
        implantCount++;
        bioMonitor.reduceHumanity(implant.getHumanityCost());

        System.out.println("🔧 Встановлено [" + implant.getSlot() + "]: " + implant.getName() +
                " (-" + implant.getHumanityCost() + " Людяності -> залишок: " + bioMonitor.getHumanity() + ")");

        if (bioMonitor.isCyberpsychosis()) {
            System.out.println("⚠️ [КРИТИЧНЕ ПОПЕРЕДЖЕННЯ BIO-MONITOR]: Людяність впала до " + bioMonitor.getHumanity() + "/100! Стан: КІБЕРПСИХОЗ (Атака x1.5, саморуйнування!)");
        }
        return true;
    }

    public int getTotalAttack() {
        int total = baseAttack;
        for (int i = 0; i < implantCount; i++) {
            total += implants[i].getAttackBonus();
        }
        if (bioMonitor.isCyberpsychosis()) {
            total = (int) (total * 1.5);
        }
        return total;
    }

    public int getTotalArmor() {
        int total = 0;
        for (int i = 0; i < implantCount; i++) {
            total += implants[i].getArmorBonus();
        }
        return total;
    }

    public void triggerAbilities() {
        for (int i = 0; i < implantCount; i++) {
            System.out.println(implants[i].activateSpecialAbility());
        }
    }

    @Override
    public String toString() {
        String modeInfo = bioMonitor.isCyberpsychosis() ? ", лють x1.5" : "";
        String info = "CyberMercenary{nick='" + nickname + "', TotalATK=" + getTotalAttack() +
                " (База: " + baseAttack + " + Імпланти: " + (getTotalAttack() / (bioMonitor.isCyberpsychosis() ? 1.5 : 1) - baseAttack) + modeInfo + "), TotalDEF=" + getTotalArmor() + "}\n" +
                "  ├─ BioMonitor: " + bioMonitor.toString() + "\n" +
                "  └─ Встановлений Хром (" + implantCount + "/" + implants.length + "):";

        for (int i = 0; i < implantCount; i++) {
            info += "\n       " + (i + 1) + ". " + implants[i].toString();
        }
        return info;
    }
}

public class Task5CyberpunkRipperdocSim {
    public static void main(String[] args) {
        System.out.println("================ 🦾 CYBERPUNK 2077: КЛІНІКА РІППЕРДОКА 🦾 ================");
        CyberMercenary david = new CyberMercenary("Девід Мартінес", 120, 1000);
        System.out.println("Пацієнт: Девід Мартінес | " + david.getBioMonitor().toString());
        System.out.println("\n[ХІРУРГІЧНЕ ВСТАНОВЛЕННЯ ІМПЛАНТІВ]:");

        david.installImplant(new SandevistanOs());
        david.installImplant(new MantisBlades());
        david.installImplant(new SandevistanOs());
        david.installImplant(new SubdermalTitaniumArmor());

        System.out.println("\n[ДОСЬЄ НАЙМАНЦЯ ПЕРЕД БОЄМ]:");
        System.out.println(david.toString());

        System.out.println("\n--- ⚔️ БІЙ НА ВЕРШИНІ ARASAKA TOWER ПРОТИ АДАМА СМЕШЕРА ⚔️ ---");

        int smasherHp = 2000;
        int smasherArmor = 40;

        for (int round = 1; round <= 3; round++) {
            System.out.println("\n[РАУНД " + round + "]");
            david.triggerAbilities();

            int playerDamage = david.getTotalAttack();
            int smasherLost = Math.max(5, playerDamage - smasherArmor);
            smasherHp -= smasherLost;
            if (smasherHp < 0) smasherHp = 0;

            System.out.println("💥 " + david.getBioMonitor().toString().split("\\|")[0].trim() + " завдає " + playerDamage + " шкоди! Адам Смешер втрачає " + smasherLost + " HP! (Залишок HP Смешера: " + smasherHp + ")");

            int bossRawDamage = 250;
            int damageTaken = david.getBioMonitor().applyDamage(bossRawDamage, david.getTotalArmor());
            System.out.println("👹 Адам Смешер контратакує на " + bossRawDamage + "! Девід отримав " + damageTaken + " чистої шкоди.");

            if (david.getBioMonitor().isCyberpsychosis()) {
                david.getBioMonitor().applyOverdriveDamage();
                System.out.println("⚠️ Нервове перевантаження! Кіберпсихоз забирає 10% HP найманця!");
            }

            System.out.println("Стан Девіда: " + david.getBioMonitor().toString());

            if (round == 2 && david.getBioMonitor().isCyberpsychosis()) {
                david.getBioMonitor().injectImmunoblockers(30);
                System.out.println("💉 Введено Імуноблокатор (+30 Людяності -> " + david.getBioMonitor().getHumanity() + "/100). Нейро-стабільність відновлено!");
            }
        }
    }
}
