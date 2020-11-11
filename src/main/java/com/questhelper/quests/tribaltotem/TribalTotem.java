/*
 * Copyright (c) 2019, Trevor <https://github.com/Trevor159>
 * Copyright (c) 2020, Zoinkwiz <https://github.com/Zoinkwiz>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.questhelper.quests.tribaltotem;

import com.questhelper.ItemCollections;
import com.questhelper.QuestHelperQuest;
import com.questhelper.Zone;
import com.questhelper.panel.PanelDetails;
import com.questhelper.steps.ConditionalStep;
import com.questhelper.steps.DetailedQuestStep;
import com.questhelper.steps.ObjectStep;
import com.questhelper.steps.conditional.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import net.runelite.api.ItemID;
import net.runelite.api.NpcID;
import net.runelite.api.NullObjectID;
import net.runelite.api.ObjectID;
import net.runelite.api.coords.WorldPoint;
import com.questhelper.requirements.ItemRequirement;
import com.questhelper.QuestDescriptor;
import com.questhelper.questhelpers.BasicQuestHelper;
import com.questhelper.steps.NpcStep;
import com.questhelper.steps.QuestStep;

@QuestDescriptor(
        quest = QuestHelperQuest.TRIBAL_TOTEM
)
public class TribalTotem extends BasicQuestHelper
{
    ItemRequirement coins, amuletOfGlory, ardougneTeleports, addressLabel, addressLabelHighlighted;

    QuestStep talkToKangaiMau, investigateCrate, useLabel, talkToEmployee, talkToCromperty, enterPassword, investigateStairs, climbStairs, searchChest, talkToKangaiMauAgain;

    ConditionForStep hasLabel;

    Zone ardougne, brimhaven, houseGroundFloor, houseFirstFloor;

    @Override
    public Map<Integer, QuestStep> loadSteps()
    {
        loadZones();
        setupItemRequirements();
        setupConditions();
        setupSteps();
        Map<Integer, QuestStep> steps = new HashMap<>();

        steps.put(0, talkToKangaiMau);

        ConditionalStep useLabelOnCrate = new ConditionalStep(this, investigateCrate);
        useLabelOnCrate.addStep(hasLabel, useLabel);
        steps.put(1, useLabelOnCrate);

        steps.put(2, talkToEmployee);
        steps.put(3, talkToCromperty);

        steps.put(4, enterPassword);
        steps.put(5, investigateStairs);
        steps.put(6, climbStairs);
        steps.put(7, searchChest);
        steps.put(8, talkToKangaiMauAgain);

        return steps;
    }

    public void setupItemRequirements()
    {
        coins = new ItemRequirement("Coins", ItemID.COINS, 90);
        amuletOfGlory = new ItemRequirement("Amulet of glory", ItemCollections.getAmuletOfGlories());
        ardougneTeleports = new ItemRequirement("Teleports to Ardougne", ItemID.ARDOUGNE_TELEPORT);
        addressLabel = new ItemRequirement("Address label", ItemID.ADDRESS_LABEL);
        addressLabelHighlighted = new ItemRequirement("Address label", ItemID.ADDRESS_LABEL);
        addressLabelHighlighted.setHighlightInInventory(true);
    }

    @Override
    public ArrayList<ItemRequirement> getItemRecommended()
    {
        ArrayList<ItemRequirement> reqs = new ArrayList<>();
        reqs.add(coins);
        reqs.add(amuletOfGlory);
        reqs.add(ardougneTeleports);
        return reqs;
    }

    public void loadZones()
    {
        ardougne = new Zone(new WorldPoint(2640, 3265, 0), new WorldPoint(2686, 3248, 0));
        brimhaven = new Zone(new WorldPoint(2744, 3205, 0), new WorldPoint(2815, 3153, 0));
        houseGroundFloor = new Zone(new WorldPoint(0, 0, 0), new WorldPoint(0, 0, 0));
        houseFirstFloor = new Zone(new WorldPoint(0, 0, 0), new WorldPoint(0, 0, 0));
    }

    public void setupConditions()
    {
        hasLabel = new ItemRequirementCondition(addressLabel);
    }

    public void setupSteps()
    {
        talkToKangaiMau = new NpcStep(this, NpcID.KANGAI_MAU, new WorldPoint(2794, 3182, 0), "Talk to Kangai Mau in the Brimhaven food store.");
        talkToKangaiMau.addDialogStep("I'm in search of adventure!");
        talkToKangaiMau.addDialogStep("Ok, I will get it back.");

        investigateCrate = new ObjectStep(this, ObjectID.CRATE_2707, new WorldPoint(2650, 3273, 0), "Travel to the GPDT depot in Ardougne and investigate the most northeastern crate for the label.");
        useLabel = new ObjectStep(this, ObjectID.CRATE_2708, new WorldPoint(2650, 3271, 0), "Use the label on the crate located 2 tiles to the south of the first crate.", addressLabel);
        useLabel.addIcon(ItemID.ADDRESS_LABEL);
        talkToEmployee = new NpcStep(this, NpcID.GPDT_EMPLOYEE, new WorldPoint(2647, 3272, 0), "Talk to a GPDT employee.");
        talkToEmployee.addDialogStep("So, when are you going to deliver this crate?");

        talkToCromperty = new NpcStep(this, NpcID.WIZARD_CROMPERTY, new WorldPoint(2683, 3326, 0), "Talk to Wizard Cromperty.");
        talkToCromperty.addDialogStep("Chat.");
        talkToCromperty.addDialogStep("So what have you invented?");
        talkToCromperty.addDialogStep("Can I be teleported please?");
        talkToCromperty.addDialogStep("Yes, that sounds good. Teleport me!");

        enterPassword = new ObjectStep(this, 0, new WorldPoint(0, 0, 0), "Go west 2 doors and enter the password KURT.");
        investigateStairs = new ObjectStep(this, 0, new WorldPoint(0, 0, 0), "Right-click Investigate the stairs.");
        climbStairs = new ObjectStep(this, 0, new WorldPoint(0, 0, 0), "Climb the stairs.");
        searchChest = new ObjectStep(this, 0, new WorldPoint(0, 0, 0), "Search the chest on the top floor for the totem.");
        investigateStairs.addDialogStep("whatever the option is to remove");
        talkToKangaiMauAgain = new NpcStep(this, NpcID.KANGAI_MAU, new WorldPoint(0, 0, 0), "Return to Kangai Mau.");
    }

    @Override
    public ArrayList<ItemRequirement> getItemRequirements()
    {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<PanelDetails> getPanels()
    {
        ArrayList<PanelDetails> allSteps = new ArrayList<>();
        allSteps.add(new PanelDetails("Retrieving the totem",
                new ArrayList<>(Arrays.asList(talkToKangaiMau, investigateCrate, useLabel, talkToEmployee, talkToCromperty, enterPassword, investigateStairs, climbStairs, searchChest, talkToKangaiMauAgain)), coins, amuletOfGlory, ardougneTeleports));

        return allSteps;
    }
}