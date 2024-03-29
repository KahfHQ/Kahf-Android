package org.thoughtcrime.securesms.stories.tabs

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.signal.core.util.logging.Log
import org.thoughtcrime.securesms.database.DatabaseObserver
import org.thoughtcrime.securesms.database.SignalDatabase
import org.thoughtcrime.securesms.dependencies.ApplicationDependencies
import org.thoughtcrime.securesms.recipients.Recipient

class ConversationListTabRepository {

  companion object {
    private val TAG = Log.tag(ConversationListTabRepository::class.java)
  }

  fun getNumberOfUnreadConversations(): Observable<Long> {
    return Observable.create<Long> {
      fun refresh() {
        it.onNext(SignalDatabase.threads.unreadThreadCount)

        val ids = SignalDatabase.threads.unreadThreadIdList
        Log.d(TAG, "Unread threads: { $ids }")
      }

      val listener = DatabaseObserver.Observer {
        refresh()
      }

      ApplicationDependencies.getDatabaseObserver().registerConversationListObserver(listener)
      it.setCancellable { ApplicationDependencies.getDatabaseObserver().unregisterObserver(listener) }

      refresh()
    }.subscribeOn(Schedulers.io())
  }

  fun getNumberOfUnseenStories(): Observable<Long> {
    return Observable.create<Long> { emitter ->
      fun refresh() {
        emitter.onNext(SignalDatabase.mms.unreadStoryThreadRecipientIds.map {
          Recipient.resolved(it)
        }.filterNot {
          if (!it.isReleaseNotes) it.shouldHideStory() else true
        }.size.toLong()
        )
      }

      val listener = DatabaseObserver.Observer {
        refresh()
      }

      ApplicationDependencies.getDatabaseObserver().registerConversationListObserver(listener)
      emitter.setCancellable { ApplicationDependencies.getDatabaseObserver().unregisterObserver(listener) }
      refresh()
    }.subscribeOn(Schedulers.io())
  }
}
