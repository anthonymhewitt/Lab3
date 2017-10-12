package pkgCore;

import java.util.ArrayList;
import java.util.Collections;

import pkgEnum.eCardNo;
import pkgEnum.eHandStrength;
import pkgEnum.eRank;
import pkgEnum.eSuit;

public class HandPoker extends Hand {

	private ArrayList<CardRankCount> CRC = null;

	public HandPoker() {
		this.setHS(new HandScorePoker());
	}

	protected ArrayList<CardRankCount> getCRC() {
		return CRC;
	}

	@Override
	public HandScore ScoreHand() {

		Collections.sort(super.getCards());
		Frequency();

		if (isRoyalFlush()) {
			return null;
		 } else if (isStraightFlush()) {
			 return null;
		 } else if (isFlush()) {
			 return null;
		 }else if (isStraight()) {
			 return null;
		 }else if (isFourOfAKind()) {
			 return null;
		 }else if (isThreeOfAKind()) {
			 return null;
		 }else if (isTwoPair()) {
			 return null;
		 }else if (isPair()) {
			 return null;
		 }else if (isHighCard()) {
			 return null;
		 }
		return null;
	}

	private void Frequency() {

		CRC = new ArrayList<CardRankCount>();

		int iCnt = 0;
		int iPos = 0;

		for (eRank eRank : eRank.values()) {
			iCnt = (CountRank(eRank));
			if (iCnt > 0) {
				iPos = FindCardRank(eRank);
				CRC.add(new CardRankCount(eRank, iCnt, iPos));
			}
		}

		Collections.sort(CRC);

		for (CardRankCount crcount : CRC) {
			System.out.print(crcount.getiCnt());
			System.out.print(" ");
			System.out.print(crcount.geteRank());
			System.out.print(" ");
			System.out.println(crcount.getiCardPosition());
		}

	}

	private int CountRank(eRank eRank) {
		int iCnt = 0;
		for (Card c : super.getCards()) {
			if (c.geteRank() == eRank) {
				iCnt++;
			}
		}
		return iCnt;
	}

	private int FindCardRank(eRank eRank) {
		int iPos = 0;

		for (iPos = 0; iPos < super.getCards().size(); iPos++) {
			if (super.getCards().get(iPos).geteRank() == eRank) {
				break;
			}
		}
		return iPos;
	}

	public boolean isRoyalFlush() {
		boolean bIsRoyalFlush = false;
		HandScorePoker HSP = (HandScorePoker) super.getHS();
		ArrayList<Card> cards = super.getCards();
		if ((this.isStraightFlush() == true)
				&& (this.getCards().get(eCardNo.FIRST.getiCardNo()).geteRank() == pkgEnum.eRank.ACE)) {
			HSP.setHiCard(cards.get(eCardNo.FIRST.getiCardNo()));
			this.setHS(HSP);
			bIsRoyalFlush = true;
		}
		

		return bIsRoyalFlush;
	}

	public boolean isStraightFlush() {
		boolean bisStraightFlush = false;
		ArrayList<Card> cards = super.getCards();
		if ((this.isFlush() == true) && (this.isStraight() == true)) {
			HandScorePoker HSP = (HandScorePoker) this.getHS();
			bisStraightFlush = true;
			HSP.seteHandStrength(eHandStrength.StraightFlush);
			HSP.setHiCard(cards.get(eCardNo.FIRST.getiCardNo()));
			this.setHS(HSP);
		}
		return bisStraightFlush;
	}

	public boolean isFourOfAKind() {
		boolean bisFourOfAKind = false;
		if (this.getCRC().size() == 2) {
			if (this.getCRC().get(0).getiCnt() == 4) {

				HandScorePoker HSP = (HandScorePoker) this.getHS();
				HSP.seteHandStrength(eHandStrength.FourOfAKind);
				bisFourOfAKind = true;

				int iGetCard = this.getCRC().get(0).getiCardPosition();

				HSP.setHiCard(this.getCards().get(iGetCard));
				HSP.setLoCard(null);

				HSP.setKickers(FindTheKickers(this.getCRC()));

				this.setHS(HSP);

			}
		}
		
		return bisFourOfAKind;
	}

	public boolean isFullHouse() {
		boolean bisFullHouse = false;

		if (this.CRC.size() == 2) {
			if ((CRC.get(0).getiCnt() == 3) && (CRC.get(1).getiCnt() == 2)) {
				bisFullHouse = true;
				HandScorePoker HSP = (HandScorePoker) this.getHS();
				HSP.seteHandStrength(eHandStrength.FullHouse);
				HSP.setHiCard(this.getCards().get(CRC.get(0).getiCardPosition()));
				HSP.setLoCard(this.getCards().get(CRC.get(1).getiCardPosition()));
				this.setHS(HSP);
			}
		}
		
		return bisFullHouse;
	}

	/**
	 * @return
	 */
	public boolean isFlush() {
		boolean bisFlush = false;
		int iCardCnt = super.getCards().size();
		int iSuitCnt = 0;
		HandScorePoker HSP = (HandScorePoker) this.getHS();
		for (eSuit eSuit : eSuit.values()) {
			for (Card c : super.getCards()) {
				if (eSuit == c.geteSuit()) {
					iSuitCnt++;
				}
			}
			if (iSuitCnt > 0) {
				break;
			}
		}
		if (iSuitCnt == iCardCnt) {
			HSP.seteHandStrength(eHandStrength.Flush);
			HSP.setHiCard(super.getCards().get(eCardNo.FIRST.getiCardNo()));
			HSP.setLoCard(null);
			this.setHS(HSP);
			HSP.setKickers(FindTheKickers(this.getCRC()));
			bisFlush = true;
		}
		else
			bisFlush = false;

		return bisFlush;
	}

	public boolean isStraight() {
		boolean bisStraight = false;
		ArrayList<Card> cards = super.getCards();
		HandScorePoker HSP = (HandScorePoker) this.getHS();
		if (this.getCRC().size() == 5) {
			if ((cards.get(eCardNo.FIRST.getiCardNo()).geteRank().getiRankNbr()) - 4 == cards
					.get(eCardNo.FIFTH.getiCardNo()).geteRank().getiRankNbr()) {
				HSP.seteHandStrength(eHandStrength.Straight);
				HSP.setHiCard(cards.get(eCardNo.FIRST.getiCardNo()));
				HSP.setLoCard(null);
				bisStraight = true;
				this.setHS(HSP);
			} else if ((cards.get(eCardNo.FIRST.getiCardNo()).geteRank() == pkgEnum.eRank.ACE)
					&& (cards.get(eCardNo.SECOND.getiCardNo()).geteRank() == pkgEnum.eRank.FIVE)
					&& (cards.get(eCardNo.FIFTH.getiCardNo()).geteRank() == pkgEnum.eRank.TWO)) {
				HSP.seteHandStrength(eHandStrength.Straight);
				HSP.setHiCard(cards.get(eCardNo.SECOND.getiCardNo()));
				HSP.setLoCard(null);
				this.setHS(HSP);
				bisStraight = true;
			}
		}

		return bisStraight;
	}

	public boolean isThreeOfAKind() {
		boolean bisThreeOfAKind = false;
		if (this.getCRC().size() == 3) {
			if (this.getCRC().get(0).getiCnt() == 3) {

				HandScorePoker HSP = (HandScorePoker) this.getHS();
				HSP.seteHandStrength(eHandStrength.ThreeOfAKind);
				bisThreeOfAKind = true;

				int iGetCard = this.getCRC().get(0).getiCardPosition();

				HSP.setHiCard(this.getCards().get(iGetCard));
				HSP.setLoCard(null);

				HSP.setKickers(FindTheKickers(this.getCRC()));

				this.setHS(HSP);

			}
		}
		return bisThreeOfAKind;
	}

	private ArrayList<Card> FindTheKickers(ArrayList<CardRankCount> CRC) {
		ArrayList<Card> kickers = new ArrayList<Card>();

		for (CardRankCount crcCheck : CRC) {
			if (crcCheck.getiCnt() == 1) {
				kickers.add(this.getCards().get(crcCheck.getiCardPosition()));
			}
		}

		return kickers;
	}

	public boolean isTwoPair() {
		ArrayList<Card> cards = super.getCards();
		boolean bisTwoPair = false;
		if (this.CRC.size() == 3) {
			if ((CRC.get(0).getiCnt() == 2) && (CRC.get(1).getiCnt() == 2) && (CRC.get(2).getiCnt() == 1)) {
				bisTwoPair = true;
				HandScorePoker HSP = (HandScorePoker) this.getHS();
				HSP.seteHandStrength(eHandStrength.TwoPair);
				HSP.setHiCard(cards.get(this.getCRC().get(0).getiCardPosition()));
				HSP.setLoCard(cards.get(this.getCRC().get(1).getiCardPosition()));
				HSP.setKickers(FindTheKickers(this.getCRC()));
				this.setHS(HSP);
			}
		}
		return bisTwoPair;
	}

	public boolean isPair() {
		boolean bisPair = false;
		ArrayList<Card> cards = super.getCards();
		if (this.CRC.size() == 4) {
			if ((CRC.get(0).getiCnt() == 2)) {
				bisPair = true;
				HandScorePoker HSP = (HandScorePoker) this.getHS();
				HSP.seteHandStrength(eHandStrength.Pair);
				HSP.setHiCard(cards.get(this.getCRC().get(0).getiCardPosition()));
				HSP.setLoCard(null);
				HSP.setKickers(FindTheKickers(this.getCRC()));
				this.setHS(HSP);
			}
		}

		return bisPair;
	}

	public boolean isHighCard() {
		ArrayList<Card> cards = super.getCards();
		boolean bisHighCard = false;
		if (this.CRC.size() == 5)
			if ((CRC.get(0).getiCnt() == 1)) {
				HandScorePoker HSP = (HandScorePoker) this.getHS();
				HSP.seteHandStrength(eHandStrength.HighCard);
				HSP.setHiCard(cards.get(eCardNo.FIRST.getiCardNo()));
				HSP.setLoCard(null);
				HSP.setKickers(FindTheKickers(this.getCRC()));
				this.setHS(HSP);
				bisHighCard = true;

			}

		return bisHighCard;
	}

}
